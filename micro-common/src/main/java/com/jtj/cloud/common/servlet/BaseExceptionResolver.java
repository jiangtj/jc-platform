package com.jtj.cloud.common.servlet;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

@Slf4j
public class BaseExceptionResolver extends DefaultHandlerExceptionResolver {

    @Resource
    ServletExceptionHandler servletExceptionHandler;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
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
