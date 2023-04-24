package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.AuthUtils;
import com.jiangtj.cloud.auth.UserClaims;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

public class JCloudExtension implements BeforeTestExecutionCallback, ParameterResolver {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return JCloudWebClientBuilder.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        AuthServer authServer = applicationContext.getBean(AuthServer.class);
        WebTestClient webTestClient = applicationContext.getBean(WebTestClient.class);
        WebTestClient.Builder builder = webTestClient.mutate();
        JCloudWebClientBuilder jCloudWebClientBuilder = new JCloudWebClientBuilder(authServer, builder);
        UserToken token = extensionContext.getRequiredTestMethod().getAnnotation(UserToken.class);
        if (token != null) {
            List<String> roles = Arrays.stream(token.role())
                .map(AuthUtils::toKey)
                .toList();
            jCloudWebClientBuilder.setClaims(new UserClaims(String.valueOf(token.id()), roles));
        }
        return jCloudWebClientBuilder;
    }
}
