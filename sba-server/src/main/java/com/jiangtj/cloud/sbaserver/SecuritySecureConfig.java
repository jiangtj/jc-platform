package com.jiangtj.cloud.sbaserver;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.net.URI;

@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {

    private final AdminServerProperties adminServer;

    public SecuritySecureConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    /*@Bean
    @Profile("insecure")
    public SecurityWebFilterChain securityWebFilterChainPermitAll(ServerHttpSecurity http) {
        return http.authorizeExchange((authorizeExchange) -> authorizeExchange.anyExchange().permitAll())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build();
    }*/

    @Bean
    public SecurityWebFilterChain securityWebFilterChainSecure(ServerHttpSecurity http) {
        return http
            .authorizeExchange(
                (authorizeExchange) -> authorizeExchange.pathMatchers(this.adminServer.path("/assets/**"))
                    .permitAll()
                    //.pathMatchers("/actuator/health/**")
                    .pathMatchers("/actuator/**")
                    .permitAll()
                    .pathMatchers(this.adminServer.path("/login"))
                    .permitAll()
                    .anyExchange()
                    .authenticated())
            .formLogin((formLogin) -> formLogin.loginPage(this.adminServer.path("/login"))
                .authenticationSuccessHandler(loginSuccessHandler(this.adminServer.path("/"))))
            .logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler(this.adminServer.path("/login?logout"))))
            .httpBasic(Customizer.withDefaults())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build();
    }

    // The following two methods are only required when setting a custom base-path (see
    // 'basepath' profile in application.yml)
    private ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(uri));
        return successHandler;
    }

    private ServerAuthenticationSuccessHandler loginSuccessHandler(String uri) {
        RedirectServerAuthenticationSuccessHandler successHandler = new RedirectServerAuthenticationSuccessHandler();
        successHandler.setLocation(URI.create(uri));
        return successHandler;
    }

}
