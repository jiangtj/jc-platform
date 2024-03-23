package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.web.ApplicationProperty;
import com.jiangtj.platform.web.aop.AnnotationMethodBeforeAdvice;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class ServerTokenMvcAdvice extends AnnotationMethodBeforeAdvice<ServerToken> implements Ordered {

    @Resource
    private ApplicationProperty applicationProperty;

    @Override
    public Class<ServerToken> getAnnotationType() {
        return ServerToken.class;
    }

    @Override
    public void before(List<ServerToken> annotations, Method method, Object[] args, Object target) {
        for (ServerToken annotation : annotations) {
            checkServerToken(annotation.value());
        }
    }

    private void checkServerToken(String[] sources) {
        AuthContext ctx = AuthHolder.getAuthContext();
        if (ServerTokenUtils.check(ctx, applicationProperty.getName(), sources)) {
            return;
        }
        throw AuthExceptionUtils.invalidToken("不支持的 Auth Context", null);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
