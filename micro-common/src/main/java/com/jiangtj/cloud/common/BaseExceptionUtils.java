package com.jiangtj.cloud.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class BaseExceptionUtils {

    /**
     * 400
     */
    public static BaseException badRequest(String msg) {
        return from(HttpStatus.BAD_REQUEST, msg, null);
    }

    /**
     * 401
     */
    public static BaseException unauthorized(String msg) {
        return from(HttpStatus.UNAUTHORIZED, msg, null);
    }

    /**
     * 403
     */
    public static BaseException forbidden(String msg) {
        return from(HttpStatus.FORBIDDEN, msg, null);
    }

    /**
     * 404
     */
    public static BaseException notFound(String msg) {
        return from(HttpStatus.NOT_FOUND, msg, null);
    }

    /**
     * 500
     */
    public static BaseException internalServerError(String msg) {
        return from(HttpStatus.INTERNAL_SERVER_ERROR, msg, null);
    }
    public static BaseException internalServerError(String msg, @Nullable Throwable cause) {
        return from(HttpStatus.INTERNAL_SERVER_ERROR, msg, cause);
    }

    public static BaseException from(HttpStatus status, String msg, @Nullable Throwable cause) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(msg);
        return new BaseException(problem, cause);
    }

}
