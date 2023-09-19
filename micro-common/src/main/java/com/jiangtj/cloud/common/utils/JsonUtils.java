package com.jiangtj.cloud.common.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //处理 java8 time api

        // 取消时间的转化格式,默认是时间戳,可以取消
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // 使用 Spring boot 容器中的 ObjectMapper 代替默认
    public static void init(ObjectMapper om) {
        objectMapper = om.copy();
    }

    public static String toJson(Object object) {

        if (object == null) {
            return null;
        }

        String json = null;

        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T fromJson(String json, Class<T> classType) {

        if (json == null) {
            return null;
        }

        T object = null;

        try {
            object = objectMapper.readValue(json, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static <T> List<T> getListFromJson(String json, Class<T> classType) {

        if (json == null) {
            return null;
        }

        List<T> lists = null;

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, classType);
            lists = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lists;
    }
}
