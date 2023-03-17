package com.jtj.cloud.authserver.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class BaseExceptionUtils {

    public static BaseException invalidToken(String msg) {
        return invalidToken(msg, null);
    }

    public static BaseException invalidToken(String msg, @Nullable Throwable cause) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problem.setTitle("无效的Token");
        problem.setDetail(msg);
        return new BaseException(problem, cause);
    }

}
