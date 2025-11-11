package com.taeyoung.studyhub.studyhub_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf((csrf) -> csrf.disable()) // JWT 사용 시 CSRF 비활성화
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                    .requestMatchers("/detail/**", "/delete/**", "/edit/**").authenticated()
                    .anyRequest().permitAll()
            )
            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint((request, response, authException) -> {
                        response.sendRedirect("/login");
                    })
            );

        return http.build();
    }
}
