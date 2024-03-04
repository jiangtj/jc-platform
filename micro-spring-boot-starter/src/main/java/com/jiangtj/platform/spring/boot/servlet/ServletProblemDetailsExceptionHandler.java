package com.jiangtj.platform.spring.boot.servlet;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class ServletProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Object[] arguments = ex.getDetailMessageArguments();
        ProblemDetail body = ex.getBody();
        body.setTitle("Invalid request content");
        body.setDetail(Arrays.stream(arguments)
            .map(String::valueOf)
            .filter(StringUtils::hasText)
            .collect(Collectors.joining(",")));
        return ResponseEntity.of(body).build();
    }
}
