package com.jtj.cloud.test;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.RequestAttributes;
import com.jtj.cloud.auth.UserClaims;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class JCloudWebClientBuilder {

    AuthServer authServer;
    WebTestClient.Builder builder;
    UserClaims claims;
    ExchangeFilterFunction filter;

    public JCloudWebClientBuilder(AuthServer authServer, WebTestClient.Builder builder) {
        this.authServer = authServer;
        this.builder = builder;
    }

    public JCloudWebClientBuilder setClaims(UserClaims claims) {
        this.claims = claims;
        return this;
    }

    public JCloudWebClientBuilder filter(ExchangeFilterFunction filter) {
        this.filter = filter;
        return this;
    }

    public WebTestClient build() {
        if (claims != null) {
            builder.defaultHeader(RequestAttributes.TOKEN_HEADER_NAME, authServer.createUserToken(claims));
        }
        if (filter != null) {
            builder.filter(filter);
        }
        return builder.build();
    }

}
