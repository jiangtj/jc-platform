package com.jtj.cloud.test;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.RequestAttributes;
import com.jtj.cloud.auth.UserClaims;
import org.springframework.test.web.reactive.server.WebTestClient;

public class JCloudWebClientBuilder {

    AuthServer authServer;
    WebTestClient.Builder builder;
    UserClaims claims;

    public JCloudWebClientBuilder(AuthServer authServer, WebTestClient.Builder builder) {
        this.authServer = authServer;
        this.builder = builder;
    }

    public void setClaims(UserClaims claims) {
        this.claims = claims;
    }

    public WebTestClient build() {
        if (claims == null) {
            return builder.build();
        }
        return builder.defaultHeader(
                RequestAttributes.TOKEN_HEADER_NAME,
                authServer.createUserToken(claims))
            .build();
    }

}
