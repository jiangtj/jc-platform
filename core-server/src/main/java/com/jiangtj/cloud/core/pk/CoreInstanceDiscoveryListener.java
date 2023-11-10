package com.jiangtj.cloud.core.pk;

import com.jiangtj.cloud.common.cloud.InstanceDiscoveryListener;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Component
public class CoreInstanceDiscoveryListener extends InstanceDiscoveryListener {

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private PublicKeyService publicKeyService;


    public void discover() {
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
