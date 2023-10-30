package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ReactiveNotifyService {


    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private AuthServer authServer;
    @Resource
    private InetUtils inetUtils;
    @Resource
    private AuthProperties properties;

    @PostConstruct
    public void setup() {
        if (!properties.isNotifyCoreServer()) {
            return;
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("core-server");
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }
        String kid = authServer.getPrivateJwk().getId();
        String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setHost(address);
        updateDto.setKid(kid);
        CompletableFuture.delayedExecutor(properties.getNotifyCoreServerDelay(), TimeUnit.SECONDS).execute(() -> {
            WebClient.create().put().uri(instances.get(0).getUri().toString() + "/service/publickey")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
        });
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }

}
