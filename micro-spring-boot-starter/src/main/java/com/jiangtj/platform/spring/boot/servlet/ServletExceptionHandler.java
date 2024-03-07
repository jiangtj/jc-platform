package com.jiangtj.platform.spring.boot.servlet;

import com.jiangtj.platform.web.BaseException;
import com.jiangtj.platform.web.BaseExceptionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ServletExceptionHandler {

    @Resource
    JsonResponseContext context;

    public void handle(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ex instanceof HandlerMethodValidationException mex) {
            URIUtils.update(mex, request);
            ProblemDetail body = mex.getBody();
            body.setTitle("Validation failure");
            body.setDetail(Arrays.stream(Objects.requireNonNull(mex.getDetailMessageArguments()))
                .map(String::valueOf)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(",")));
            ServerResponse.status(body.getStatus())
                .body(body)
                .writeTo(request, response, context);
            log.warn(buildLogMessage(ex, request));
            return;
        }

        if (ex instanceof ErrorResponseException bex) {
            URIUtils.update(bex, request);
            ServerResponse.from(bex).writeTo(request, response, context);
            log.warn(buildLogMessage(ex, request));
        } else {
            BaseException wrapper = BaseExceptionUtils.internalServerError(ex.getMessage(), ex);
            URIUtils.update(wrapper, request);
            ServerResponse.from(wrapper).writeTo(request, response, context);
            log.error(buildLogMessage(ex, request));
        }
    }

    /**
     * Build a log message for the given exception, occurred during processing the given request.
     * @param ex the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @return the log message to use
     */
    protected String buildLogMessage(Exception ex, HttpServletRequest request) {
        return "Resolved [" + LogFormatUtils.formatValue(ex, -1, true) + "]";
    }

}
