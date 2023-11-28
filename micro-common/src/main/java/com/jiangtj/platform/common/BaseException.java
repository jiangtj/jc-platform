package com.jiangtj.platform.common;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponseException;

public class BaseException extends ErrorResponseException {

    public BaseException(HttpStatusCode status) {
        super(status);
    }

    public BaseException(ProblemDetail body) {
        this(body, null);
    }

    public BaseException(ProblemDetail body, @Nullable Throwable cause) {
        super(HttpStatusCode.valueOf(body.getStatus()), body, cause);
    }

}
