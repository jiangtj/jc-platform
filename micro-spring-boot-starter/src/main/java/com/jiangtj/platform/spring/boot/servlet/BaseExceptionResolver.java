package com.jiangtj.platform.spring.boot.servlet;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.io.IOException;

public class BaseExceptionResolver extends AbstractHandlerExceptionResolver {

    @Resource
    ServletExceptionHandler servletExceptionHandler;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        if (ex instanceof RuntimeException rex) {
            try {
                servletExceptionHandler.handle(rex, request, response);
                return new ModelAndView();
            } catch (ServletException | IOException e) {
                // ignore
            }
        }
        // HttpRequestMethodNotSupportedException
        return null;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
