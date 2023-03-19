package com.jtj.cloud.common.servlet;

import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import com.jtj.cloud.common.BaseException;

public class BaseExceptionFilter implements HandlerFilterFunction {
  @Override
  public ServerResponse filter(ServerRequest request, HandlerFunction next) throws Exception {
    try {
      return next.handle(request);
    } catch (BaseException e) {
      return ServerResponse.from(e);
    }
  }

}