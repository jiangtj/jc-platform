package com.jiangtj.platform.core.pk;

import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.spring.cloud.InstanceDiscoveryListener;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

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
        List<String> services = discoveryClient.getServices();
        List<ServiceInstance> instanceList = services.stream()
            .flatMap(s -> discoveryClient.getInstances(s).stream())
            .toList();

        List<String> ids = instanceList.stream().map(ServiceInstance::getInstanceId).toList();
        log.info(JsonUtils.toJson(ids));
        List<MicroServiceData> allCoreServiceInstance = publicKeyService.getAllCoreServiceInstance();
        allCoreServiceInstance.stream()
            .filter(instance -> !ids.contains(instance.getInstanceId()))
            .forEach(instance -> {
                instance.setStatus(MicroServiceData.Status.Down);
            });

        instanceList.stream()
            .map(publicKeyService::getCoreServiceInstance)
            .forEach(publicKeyService::updateCoreServiceInstance);
    }
}
