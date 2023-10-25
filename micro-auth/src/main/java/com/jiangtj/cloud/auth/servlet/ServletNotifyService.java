package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServletNotifyService {

    @Resource
    @LoadBalanced
    private RestTemplate loadBalanced;
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
        String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setHost(address);
        updateDto.setKid(authServer.getPrivateJwk().getId());
        HttpEntity<UpdateDto> entity = new HttpEntity<>(updateDto);
        CompletableFuture.delayedExecutor(properties.getNotifyCoreServerDelay(), TimeUnit.SECONDS).execute(() -> {
            loadBalanced.put("http://core-server/service/publickey", entity);
        });
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }

}
