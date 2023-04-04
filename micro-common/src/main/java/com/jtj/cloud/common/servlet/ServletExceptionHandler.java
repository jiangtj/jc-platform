package com.jtj.cloud.common.servlet;

import com.jtj.cloud.common.BaseException;
import com.jtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

public class ServletExceptionHandler {

    @Resource
    JsonResponseContext context;

    public void handle(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
