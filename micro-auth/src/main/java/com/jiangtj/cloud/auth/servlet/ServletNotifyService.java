package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.CoreInstanceService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Deprecated
public class ServletNotifyService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Resource
    private AuthServer authServer;
    @Resource
    private InetUtils inetUtils;
    @Resource
    private AuthProperties properties;
    @Resource
    private CoreInstanceService coreInstanceService;

    @PostConstruct
    public void setup() {
        if (!properties.isNotifyCoreServer()) {
            return;
        }
        CompletableFuture.delayedExecutor(properties.getNotifyCoreServerDelay(), TimeUnit.SECONDS).execute(() -> {
            coreInstanceService.getUri().ifPresentOrElse(uri -> {
                String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
                UpdateDto updateDto = new UpdateDto();
                updateDto.setHost(address);
                updateDto.setKid(authServer.getPrivateJwk().getId());
                HttpEntity<UpdateDto> entity = new HttpEntity<>(updateDto);
                restTemplate.put(uri + "/service/publickey", entity);
            }, () -> {
                log.warn("Core Server isn't running!");
            });
        });
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }

}
