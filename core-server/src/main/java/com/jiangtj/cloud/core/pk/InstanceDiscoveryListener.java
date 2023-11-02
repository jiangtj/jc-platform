package com.jiangtj.cloud.core.pk;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.discovery.event.ParentHeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Component
public class InstanceDiscoveryListener {

    private final HeartbeatMonitor monitor = new HeartbeatMonitor();

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private PublicKeyService publicKeyService;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        discover();
    }

    @EventListener
    public void onInstanceRegistered(InstanceRegisteredEvent<?> event) {
        discover();
    }

    @EventListener
    public void onParentHeartbeat(ParentHeartbeatEvent event) {
        discoverIfNeeded(event.getValue());
    }

    @EventListener
    public void onApplicationEvent(HeartbeatEvent event) {
        discoverIfNeeded(event.getValue());
    }

    private void discoverIfNeeded(Object value) {
        if (this.monitor.update(value)) {
            discover();
        }
    }

    protected void discover() {
        log.info("discovering client change.");
        Flux.fromIterable(discoveryClient.getServices())
            .flatMapIterable(s -> discoveryClient.getInstances(s))
            .collectList()
            .doOnNext(instances -> {
                List<String> ids = instances.stream().map(ServiceInstance::getInstanceId).toList();
                List<MicroServiceData> allCoreServiceInstance = publicKeyService.getAllCoreServiceInstance();
                allCoreServiceInstance.stream()
                    .filter(instance -> !ids.contains(instance.getInstanceId()))
                    .forEach(instance -> {
                        instance.setStatus(MicroServiceData.Status.Down);
                    });
            })
            .flatMapMany(Flux::fromIterable)
            .map(publicKeyService::getCoreServiceInstance)
            .subscribe(publicKeyService::updateCoreServiceInstance);
    }

}
