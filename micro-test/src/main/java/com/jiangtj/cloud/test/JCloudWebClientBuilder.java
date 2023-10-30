package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

public class JCloudWebClientBuilder {

    AuthServer authServer;
    WebTestClient.Builder builder;
    Long id;
    List<String> roles;
    ExchangeFilterFunction filter;

    public JCloudWebClientBuilder(AuthServer authServer, WebTestClient.Builder builder) {
        this.authServer = authServer;
        this.builder = builder;
    }

    public JCloudWebClientBuilder setUser(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
        return this;
    }

    public JCloudWebClientBuilder filter(ExchangeFilterFunction filter) {
        this.filter = filter;
        return this;
    }

    public WebTestClient build() {
        if (id != null) {
            builder.defaultHeader(AuthRequestAttributes.TOKEN_HEADER_NAME, authServer.createUserToken(String.valueOf(id), roles, "test"));
        }
        if (filter != null) {
            builder.filter(filter);
        }
        return builder.build();
    }

}
