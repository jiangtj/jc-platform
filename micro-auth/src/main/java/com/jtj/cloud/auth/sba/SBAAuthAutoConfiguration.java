package com.jtj.cloud.auth.sba;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.auth.rbac.Role;
import de.codecentric.boot.admin.server.config.AdminServerMarkerConfiguration;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static com.jtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

@Slf4j
@AutoConfiguration
public class SBAAuthAutoConfiguration {

    @AutoConfiguration
    @ConditionalOnBean({AdminServerMarkerConfiguration.Marker.class})
    static class SBAServerAutoConfiguration {
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
        public Role actuatorRole() {
            return RoleInst.ACTUATOR;
        }
    }

    @AutoConfiguration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class SBAReactorConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public AuthActuatorReactorWebFilter authActuatorReactorWebFilter(List<Role> roles) {
            return new AuthActuatorReactorWebFilter();
        }
    }

}
