package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.RoleProvider;
import com.jiangtj.platform.auth.context.RoleProviderAuthContext;
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
        if (!annotation.inheritRoleProvider()) {
            return new SimpleTestAuthContext(annotation.subject(),
                List.of(annotation.roles()),
                List.of(annotation.permissions()));
        }

        RoleProvider roleProvider = TestAuthContextHolder.getProvider();
        if (roleProvider != null) {
            return RoleProviderAuthContext.create(annotation.subject(), roleProvider, List.of(annotation.roles()), List.of(annotation.permissions()));
        }

        ObjectProvider<RoleProvider> provider = context.getBeanProvider(RoleProvider.class);
        RoleProvider unique = provider.getIfUnique();
        if (unique == null) {
            return new SimpleTestAuthContext(annotation.subject(),
                List.of(annotation.roles()),
                List.of(annotation.permissions()));
        }

        return RoleProviderAuthContext.create(annotation.subject(), unique, List.of(annotation.roles()), List.of(annotation.permissions()));
    }
}
