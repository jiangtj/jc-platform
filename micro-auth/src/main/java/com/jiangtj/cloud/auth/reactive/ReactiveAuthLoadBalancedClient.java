package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import com.jiangtj.cloud.auth.AuthServer;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.PublicKey;

public class ReactiveAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    @LoadBalanced
    private WebClient.Builder loadBalanced;

    @Resource
    private AuthServer authServers;

    @Resource
    private InetUtils inetUtils;

    @Resource
    private ReactiveCachedPublicKeyService reactiveCachedPublicKeyService;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {

        return reactiveCachedPublicKeyService.getPublicJwk(kid);
    }

    @Override
    public void notifyUpdatePublicJwk(String kid) {
        String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setHost(address);
        updateDto.setKid(kid);
//        CompletableFuture.delayedExecutor(15, TimeUnit.SECONDS).execute(() -> {
//            try {
//                loadBalanced.build().put().uri("http://core-server/service/publickey", kid)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(updateDto)
//                    .retrieve()
//                    .toBodilessEntity()
//                    .toFuture()
//                    .get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }
}
