package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * 验证TOKEN
 * Created by maokefeng on 2017/3/28.
 */
@Slf4j
public class ServletTokenMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private ServletTokenInterceptor servletTokenInterceptor;
    @Resource
    private AuthServer authServer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(servletTokenInterceptor);
        registration.addPathPatterns("/**");
        List<String> excludePatterns = authServer.getProperties().getExcludePatterns();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            registration.excludePathPatterns(excludePatterns);
        }
    }

}
