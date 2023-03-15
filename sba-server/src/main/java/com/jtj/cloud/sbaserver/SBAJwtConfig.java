package com.jtj.cloud.sbaserver;

import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Created At 2021/3/24.
 */
@Slf4j
@Configuration
public class SBAJwtConfig {

    @Bean
    public HttpHeadersProvider customHttpHeadersProvider(AuthServer authServer) {
        return instance -> {
            String instanceName = instance.getRegistration().getName();
            HttpHeaders httpHeaders = new HttpHeaders();
            String header = authServer.builder()
                .setAudience(instanceName)
                .build();
            httpHeaders.add(authServer.getProperties().getHeaderName(), header);
            return httpHeaders;
        };
    }

}
