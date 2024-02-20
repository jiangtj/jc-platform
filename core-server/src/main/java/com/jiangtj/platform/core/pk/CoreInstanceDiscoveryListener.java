package com.jiangtj.platform.core.pk;

import com.jiangtj.platform.auth.cloud.InstanceDiscoveryListener;
import com.jiangtj.platform.common.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Component
public class CoreInstanceDiscoveryListener extends InstanceDiscoveryListener {

    @Resource
    private ReactiveDiscoveryClient discoveryClient;

    @Resource
    private PublicKeyService publicKeyService;


    public void discover() {
        log.info("discovering client change.");
        discoveryClient.getServices()
            .flatMap(s -> discoveryClient.getInstances(s))
            .collectList()
            .doOnNext(instances -> {
                List<String> ids = instances.stream().map(ServiceInstance::getInstanceId).toList();
                log.info(JsonUtils.toJson(ids));
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
