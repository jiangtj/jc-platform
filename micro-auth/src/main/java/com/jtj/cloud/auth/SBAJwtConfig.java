package com.jtj.cloud.auth;

import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

/**
 * Created At 2021/3/24.
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(HttpHeadersProvider.class)
public class SBAJwtConfig {

    @Bean
    public HttpHeadersProvider customHttpHeadersProvider(AuthServer authServer) {
        return instance -> {
            String instanceName = instance.getRegistration().getName();
            HttpHeaders httpHeaders = new HttpHeaders();
            String header = authServer.builder()
                .setAudience(instanceName)
                .setAuthType(TokenType.SERVER)
                .build();
            httpHeaders.add(authServer.getProperties().getHeaderName(), header);
            log.error("token:---" + header);
            return httpHeaders;
        };
    }

}
