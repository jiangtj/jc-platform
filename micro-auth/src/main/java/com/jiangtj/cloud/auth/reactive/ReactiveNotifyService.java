package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ReactiveNotifyService {

    @Resource
    @LoadBalanced
    private WebClient.Builder loadBalanced;
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
        String kid = authServer.getPrivateJwk().getId();
        String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setHost(address);
        updateDto.setKid(kid);
        CompletableFuture.delayedExecutor(properties.getNotifyCoreServerDelay(), TimeUnit.SECONDS).execute(() -> {
            loadBalanced.build().put().uri("http://core-server/service/publickey")
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
