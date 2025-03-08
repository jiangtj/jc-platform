package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class SimpleTestAuthContextConverter implements TestAnnotationConverter<WithMockUser> {

    @Override
    public Class<WithMockUser> getAnnotationClass() {
        return WithMockUser.class;
    }

    @Override
    public AuthContext convert(WithMockUser annotation, ApplicationContext context) {
        Subject subject = new Subject();
        subject.setId(annotation.subject());

        if (!annotation.inheritRoleProvider()) {
            return AuthContext.create(subject, Authorization.create(
                List.of(annotation.roles()),
                List.of(annotation.permissions())));
        }

        RoleProvider roleProvider = TestAuthContextHolder.getProvider();
        if (roleProvider != null) {
            return AuthContext.create(subject, RbacAuthorization.create(roleProvider, List.of(annotation.roles()), List.of(annotation.permissions())));
        }

        ObjectProvider<RoleProvider> provider = context.getBeanProvider(RoleProvider.class);
        RoleProvider unique = provider.getIfUnique();
        if (unique == null) {
            return AuthContext.create(subject, Authorization.create(
                List.of(annotation.roles()),
                List.of(annotation.permissions())));
        }

        return AuthContext.create(subject, RbacAuthorization.create(unique, List.of(annotation.roles()), List.of(annotation.permissions())));
    }
}
