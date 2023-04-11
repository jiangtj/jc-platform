package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.RBACAutoConfiguration;
import com.jtj.cloud.auth.rbac.Role;
import com.jtj.cloud.auth.rbac.RoleContext;
import com.jtj.cloud.auth.rbac.RoleInst;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.jtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

/**
 * Created At 2021/3/24.
 */
@Slf4j
@AutoConfiguration(before = RBACAutoConfiguration.class)
@ConditionalOnClass(HttpHeadersProvider.class)
public class SBAAuthAutoConfiguration {

    @Bean
    public HttpHeadersProvider customHttpHeadersProvider(AuthServer authServer) {
        return instance -> {
            String instanceName = instance.getRegistration().getName();
            HttpHeaders httpHeaders = new HttpHeaders();
            String header = authServer.builder()
                .setAudience(instanceName)
                .setAuthType(TokenType.SERVER)
                .build();
            httpHeaders.add(TOKEN_HEADER_NAME, header);
            log.error("token:---" + header);
            return httpHeaders;
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public RoleContext roleContext(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return () -> Collections.singletonList(RoleInst.ACTUATOR.role());
        }
        return () -> roles;
    }

}
