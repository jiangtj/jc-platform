package com.jiangtj.platform.spring.cloud.sba;

import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.system.Role;
import de.codecentric.boot.admin.server.config.AdminServerMarkerConfiguration;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

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
                String header = authServer.createServerToken(instanceName);
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

}
