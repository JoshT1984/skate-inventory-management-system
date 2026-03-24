package com.skillstorm.skate_inventory_mgmt_system.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.skillstorm.skate_inventory_mgmt_system.services.CustomOAuth2UserService;
import com.skillstorm.skate_inventory_mgmt_system.services.CustomOidcUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Value("${app.frontend-url:http://localhost:4200}")
        private String frontendUrl;

        @Bean
        SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        CustomOAuth2UserService customOAuth2UserService,
                        CustomOidcUserService customOidcUserService) throws Exception {

                CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
                csrfTokenRepository.setCookiePath("/");
                csrfTokenRepository.setCookieName("XSRF-TOKEN");
                csrfTokenRepository.setHeaderName("X-XSRF-TOKEN");

                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(csrfTokenRepository)
                                                .ignoringRequestMatchers("/health"))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/error", "/health", "/csrf", "/oauth2/**",
                                                                "/login/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/auth/me").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/products/**", "/warehouses/**",
                                                                "/warehouse-inventory/**")
                                                .hasAnyRole("GUEST", "EMPLOYEE", "MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/warehouse-inventory/transfer")
                                                .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/warehouse-inventory/**")
                                                .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/warehouse-inventory/**")
                                                .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/warehouse-inventory/**")
                                                .hasAnyRole("MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/products/**", "/warehouses/**")
                                                .hasAnyRole("MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/products/**", "/warehouses/**")
                                                .hasAnyRole("MANAGER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/products/**", "/warehouses/**")
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth -> oauth
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService)
                                                                .oidcUserService(customOidcUserService))
                                                .successHandler((request, response, authentication) -> {
                                                        String targetUrl = frontendUrl + "/warehouse-inventory";
                                                        System.out.println("=== OAuth success handler hit ===");
                                                        System.out.println("Redirecting to: " + targetUrl);
                                                        response.sendRedirect(targetUrl);
                                                })
                                                .failureHandler((request, response, exception) -> {
                                                        System.out.println("=== OAuth failure handler hit ===");
                                                        System.out.println("Exception type: "
                                                                        + exception.getClass().getName());
                                                        System.out.println(
                                                                        "Exception message: " + exception.getMessage());
                                                        exception.printStackTrace();
                                                        response.sendRedirect(frontendUrl + "/login?error=oauth");
                                                }))
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .logoutSuccessHandler((request, response, authentication) -> response
                                                                .setStatus(204)))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint((request, response, authException) -> response
                                                                .sendError(401))
                                                .accessDeniedHandler((request, response,
                                                                accessDeniedException) -> response.sendError(403)));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(frontendUrl, "http://localhost:4200"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(List.of("Set-Cookie"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}