package com.jiangtj.platform.spring.boot.servlet;

import com.jiangtj.platform.web.BaseException;
import com.jiangtj.platform.web.BaseExceptionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServletExceptionHandler {

    @Resource
    JsonResponseContext context;

    public void handle(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ex instanceof HandlerMethodValidationException mex) {
            ProblemDetail body = mex.getBody();
            body.setTitle("Validation failure");
            body.setDetail(Arrays.stream(Objects.requireNonNull(mex.getDetailMessageArguments()))
                .map(String::valueOf)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(",")));
            ServerResponse.status(body.getStatus())
                .body(body)
                .writeTo(request, response, context);
            return;
        }

        if (ex instanceof ErrorResponseException bex) {
            URIUtils.update(bex, request);
            ServerResponse.from(bex).writeTo(request, response, context);
        } else {
            BaseException wrapper = BaseExceptionUtils.internalServerError(ex.getMessage(), ex);
            URIUtils.update(wrapper, request);
            ServerResponse.from(wrapper).writeTo(request, response, context);
        }
    }

}
