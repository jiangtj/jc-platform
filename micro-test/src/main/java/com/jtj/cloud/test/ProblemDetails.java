package com.jtj.cloud.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public interface ProblemDetails {

    static ProblemDetail unLogin(String uri) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        detail.setTitle("未登录");
        detail.setDetail("缺少认证信息，请在header中携带token");
        detail.setInstance(URI.create(uri));
        return detail;
    }

}
