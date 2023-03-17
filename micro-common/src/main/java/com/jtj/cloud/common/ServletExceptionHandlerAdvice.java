
package com.jtj.cloud.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created At 2020/10/15.
 */
@Slf4j
@ControllerAdvice
public class ServletExceptionHandlerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleException(BaseException e) {
        log.error(e.toString(), e);
        return ResponseEntity.of(e.getBody()).build();
    }

    /*@ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ErrorResult> handleJwtException(FeignException.BadRequest badRequest) {
        String body = badRequest.contentUTF8();
        ErrorResult errorResult = null;
        try {
            errorResult = objectMapper.readValue(body, ErrorResult.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }*/

}
