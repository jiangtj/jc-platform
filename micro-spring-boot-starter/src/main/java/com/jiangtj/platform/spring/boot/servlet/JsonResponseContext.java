package com.jiangtj.platform.spring.boot.servlet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.ArrayList;
import java.util.List;

public class JsonResponseContext implements ServerResponse.Context, ApplicationContextAware {

    private ApplicationContext applicationContext;

    List<HttpMessageConverter<?>> messageConverters;

    public JsonResponseContext() {
        messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
        if (this.applicationContext != null) {
            builder.applicationContext(this.applicationContext);
        }
        messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Override
    public List<HttpMessageConverter<?>> messageConverters() {
        return messageConverters;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
