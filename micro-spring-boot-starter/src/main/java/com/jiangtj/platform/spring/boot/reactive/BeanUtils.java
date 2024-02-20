package com.jiangtj.platform.spring.boot.reactive;

import org.springframework.web.bind.support.WebExchangeDataBinder;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public interface BeanUtils {

    static <T> Mono<T> convertParams(ServerRequest request, T defaultValue) {
        Mono<Void> bind = new WebExchangeDataBinder(defaultValue).bind(request.exchange());
        return bind.then(Mono.just(defaultValue))
            .doOnNext(item -> {
                PropertyDescriptor[] descriptors = org.springframework.beans.BeanUtils.getPropertyDescriptors(item.getClass());
                for (PropertyDescriptor descriptor: descriptors) {
                    if (descriptor.getPropertyType().isAssignableFrom(String.class)) {
                        try {
                            Object invoke = descriptor.getReadMethod().invoke(item);
                            if (invoke == null) {
                                continue;
                            }
                            descriptor.getWriteMethod().invoke(item, URLDecoder.decode((String) invoke, StandardCharsets.UTF_8));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}
