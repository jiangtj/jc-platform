package com.jtj.cloud.common.reactive;

import jakarta.annotation.Resource;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * Created At 2021/3/26.
 */
public class NoViewResponseContext implements ServerResponse.Context {

    @Resource
    ServerCodecConfigurer serverCodecConfigurer;

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return serverCodecConfigurer.getWriters();
    }

    @Override
    public List<ViewResolver> viewResolvers() {
        return Collections.emptyList();
    }
}
