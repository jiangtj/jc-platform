package com.jtj.cloud.common.servlet;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created At 2021/3/26.
 */
public class NoViewResponseContext implements ServerResponse.Context {

    List<HttpMessageConverter<?>> messageConverters;

    public NoViewResponseContext() {
        messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public List<HttpMessageConverter<?>> messageConverters() {
        return messageConverters;
    }
}