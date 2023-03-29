package com.jtj.cloud.common.servlet;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

public class BaseExceptionResolver extends DefaultHandlerExceptionResolver {

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
        return super.doResolveException(request, response, handler, ex);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
