package com.jiangtj.platform.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProblemDetailConsumer {

    private final ProblemDetail detail;

    public static ProblemDetailConsumer unLogin() {
        return new ProblemDetailConsumer(HttpStatus.UNAUTHORIZED)
            .title("No Login")
            .detail("You have to take a token for this request.");
    }

    //{"type":"about:blank","title":"Invalid Token","status":403,"detail":"不支持的 Auth Context","instance":"/actuator"}
    public static ProblemDetailConsumer unInvalid(String msg) {
        return new ProblemDetailConsumer(HttpStatus.FORBIDDEN)
                .title("Invalid Token")
                .detail(msg);
    }

    //{"type":"about:blank","title":"No Role","status":403,"detail":"Don't have role<roletest2>.","instance":"/role-test-2"}
    public static ProblemDetailConsumer unRole(String role) {
        return new ProblemDetailConsumer(HttpStatus.FORBIDDEN)
                .title("No Role")
                .detail(String.format("Don't have role<%s>.", role));
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

    //{"type":"about:blank","title":"Validation failure","status":400,"detail":"手机号格式不正确"}
    public static WebTestClient.ResponseSpec.ResponseSpecConsumer unValidation(String... failures) {
        return responseSpec -> {
            responseSpec.expectStatus().is4xxClientError();
            responseSpec.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
            responseSpec.expectBody()
                .jsonPath("type").exists()
                .jsonPath("status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("title").isEqualTo("Validation failure")
                .jsonPath("detail").value(d -> {
                    for (String failure : failures) {
                        assertTrue(d.contains(failure));
                    }
                }, String.class)
                .jsonPath("instance").exists();
        };
    }

}
