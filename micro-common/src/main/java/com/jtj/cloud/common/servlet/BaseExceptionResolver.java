package com.jtj.cloud.common.servlet;

import com.jtj.cloud.common.BaseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

public class BaseExceptionResolver extends DefaultHandlerExceptionResolver {

    @Resource
    NoViewResponseContext context;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof BaseException bex) {
            try {
                ServerResponse.from(bex).writeTo(request, response, context);
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
