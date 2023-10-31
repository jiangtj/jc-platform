package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.CoreInstanceService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReactiveNotifyService {

    private final WebClient webClient = WebClient.create();
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
                String kid = authServer.getPrivateJwk().getId();
                String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
                UpdateDto updateDto = new UpdateDto();
                updateDto.setHost(address);
                updateDto.setKid(kid);
                webClient.put().uri(uri + "/service/publickey")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe();
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
