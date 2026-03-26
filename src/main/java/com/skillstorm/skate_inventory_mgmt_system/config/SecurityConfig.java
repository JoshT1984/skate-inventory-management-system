package com.skillstorm.skate_inventory_mgmt_system.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.skillstorm.skate_inventory_mgmt_system.services.CustomOAuth2UserService;
import com.skillstorm.skate_inventory_mgmt_system.services.CustomOidcUserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

        private final CustomOAuth2UserService customOAuth2UserService;
        private final CustomOidcUserService customOidcUserService;

        @Value("${app.frontend-url:http://localhost:4200}")
        private String frontendUrl;

        public SecurityConfig(
                        CustomOAuth2UserService customOAuth2UserService,
                        CustomOidcUserService customOidcUserService) {
                this.customOAuth2UserService = customOAuth2UserService;
                this.customOidcUserService = customOidcUserService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/actuator/health",
                                                                "/error",
                                                                "/login",
                                                                "/login/**",
                                                                "/oauth2/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth -> oauth
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService)
                                                                .oidcUserService(customOidcUserService))
                                                .successHandler(oAuth2SuccessHandler())
                                                .failureHandler(oAuth2FailureHandler()))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        log.info("Logout success for user={}",
                                                                        authentication != null
                                                                                        ? authentication.getName()
                                                                                        : "anonymous");
                                                        response.sendRedirect(frontendUrl + "/login");
                                                })
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"));

                return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler oAuth2SuccessHandler() {
                return (request, response, authentication) -> {
                        log.info("OAuth login success. principalClass={}, name={}",
                                        authentication.getPrincipal().getClass().getName(),
                                        authentication.getName());

                        Object principal = authentication.getPrincipal();

                        if (principal instanceof OidcUser oidcUser) {
                                log.info("OIDC user claims: sub={}, email={}, name={}",
                                                oidcUser.getSubject(),
                                                oidcUser.getEmail(),
                                                oidcUser.getFullName());
                        } else if (principal instanceof OAuth2User oauth2User) {
                                log.info("OAuth2 user attributes keys={}", oauth2User.getAttributes().keySet());
                                log.info("OAuth2 user email={}, name={}, sub={}",
                                                oauth2User.getAttribute("email"),
                                                oauth2User.getAttribute("name"),
                                                oauth2User.getAttribute("sub"));
                        }

                        response.sendRedirect(frontendUrl + "/warehouse-inventory");
                };
        }

        @Bean
        public AuthenticationFailureHandler oAuth2FailureHandler() {
                return (request, response, exception) -> {
                        log.error("OAuth login failed. requestUri={}, message={}",
                                        request.getRequestURI(),
                                        exception.getMessage(),
                                        exception);

                        response.sendRedirect("/api/login?error");
                };
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOriginPatterns(List.of(
                                "http://localhost:4200",
                                "http://localhost:8080",
                                "http://skate-api-prod.eba-ixt4pv9i.us-east-1.elasticbeanstalk.com",
                                "https://*"));

                config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                config.setExposedHeaders(List.of("Set-Cookie", "Location"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}