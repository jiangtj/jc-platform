package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import com.jiangtj.cloud.auth.AuthServer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@TestConfiguration(proxyBeanMethods = false)
public class JCloudAuthConfiguration {

    @Bean
    public AuthLoadBalancedClient authLoadBalancedClient(ObjectProvider<AuthServer> authServers) {
        return kid -> {
            return Objects.requireNonNull(authServers.getIfUnique()).getPrivateJwk().toPublicJwk();
        };
    }

}
