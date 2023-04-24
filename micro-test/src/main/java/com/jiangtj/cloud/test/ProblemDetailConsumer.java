package com.jiangtj.cloud.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ProblemDetailConsumer {

    private final ProblemDetail detail;

    public static ProblemDetailConsumer unLogin() {
        return new ProblemDetailConsumer(HttpStatus.UNAUTHORIZED)
            .title("No Login")
            .detail("You have to take a token for this request.");
    }

    //{"type":"about:blank","title":"No Permission","status":403,"detail":"Don't have permission<system:user:write>","instance":"/user"}
    public static ProblemDetailConsumer unPermission(String permissionName) {
        return new ProblemDetailConsumer(HttpStatus.FORBIDDEN)
            .title("No Permission")
            .detail(String.format("Don't have permission<%s>.", permissionName));
    }

    public static ProblemDetailConsumer forStatus(HttpStatus status) {
        return new ProblemDetailConsumer(status)
            .title(status.getReasonPhrase());
    }

    ProblemDetailConsumer(HttpStatusCode code) {
        detail = ProblemDetail.forStatus(code);
    }

    public ProblemDetailConsumer title(String title) {
        this.detail.setTitle(title);
        return this;
    }

    public ProblemDetailConsumer detail(String detail) {
        this.detail.setDetail(detail);
        return this;
    }

    public WebTestClient.ResponseSpec.ResponseSpecConsumer expect() {
        return responseSpec -> {
            responseSpec.expectStatus().is4xxClientError();
            responseSpec.expectStatus().isEqualTo(detail.getStatus());
            responseSpec.expectBody()
                .jsonPath("type").exists()
                .jsonPath("status").isEqualTo(detail.getStatus())
                .jsonPath("title").isEqualTo(detail.getTitle())
                .jsonPath("detail").isEqualTo(detail.getDetail())
                .jsonPath("instance").exists();
        };
    }

}
