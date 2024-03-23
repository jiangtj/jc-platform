package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContextFactory;
import com.jiangtj.platform.test.TestAnnotationConverter;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;

public class WithServerConverter implements TestAnnotationConverter<WithServer> {

    @Resource
    private JwtAuthContextFactory factory;
    @Resource
    private AuthTestServer authTestServer;

    @Override
    public Class<WithServer> getAnnotationClass() {
        return WithServer.class;
    }

    @Override
    public AuthContext convert(WithServer annotation, ApplicationContext context) {
        String iss = annotation.value();
        String token = authTestServer.createServerTokenFrom(iss);
        return factory.getAuthContext(token);
    }
}
