package com.jtj.cloud.basereactive;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
public class ProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {
}
