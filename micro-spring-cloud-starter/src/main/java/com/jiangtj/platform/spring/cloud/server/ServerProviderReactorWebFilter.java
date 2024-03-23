package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.platform.auth.reactive.AuthReactorHandler;
import com.jiangtj.platform.web.ApplicationProperty;
import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

import java.util.List;

public class ServerProviderReactorWebFilter extends AuthReactiveWebFilter {

    @Resource
    private ApplicationProperty applicationProperty;
    @Resource
    private ServerProviderProperties properties;

    @Override
    public List<String> getIncludePatterns() {
        return properties.getPath();
    }

    @Override
    public List<String> getExcludePatterns() {
        return properties.getExclude();
    }

    @Override
    public void filter(AuthReactorHandler handler) {
        handler.hasLogin()
            .filter(ctx -> {
                if (ctx instanceof ServerContextImpl sc) {
                    if (!sc.hasAudience(applicationProperty.getName())) {
                        return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                    }
                    return Mono.just(ctx);
                } else {
                    return Mono.error(AuthExceptionUtils.invalidToken("不支持的 Auth Context", null));
                }
            });
    }
}
