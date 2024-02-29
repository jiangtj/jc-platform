package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContextFactory;
import com.jiangtj.platform.test.TestAnnotationConverter;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Stream;

public class UserTokenConverter implements TestAnnotationConverter<UserToken> {

    @Resource
    private JwtAuthContextFactory factory;
    @Resource
    private AuthServer authServer;

    @Override
    public Class<UserToken> getAnnotationClass() {
        return UserToken.class;
    }

    @Override
    public AuthContext convert(UserToken annotation, ApplicationContext context) {
        long id = annotation.id();
        List<String> roles = Stream.of(annotation.role())
            .map(KeyUtils::toKey)
            .toList();
        String test = authServer.createUserToken(String.valueOf(id), roles, "test");
        return factory.getAuthContext(test);
    }
}
