package com.taeyoung.studyhub.studyhub_backend.config;

import com.taeyoung.studyhub.studyhub_backend.jwt.JwtFilter;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/my-page/**").authenticated()
                .anyRequest().permitAll()
        );
        // auth 없이 지정된 경로 접속 시 /login으로 이동
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login");
                })
        );

        http.addFilterBefore(new JwtFilter(), ExceptionTranslationFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/logout") // 기본 로그아웃 URL
                .logoutSuccessHandler((request, response, authentication) -> {
                    // ✅ JWT 쿠키 삭제
                    Cookie cookie = new Cookie("jwt", null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);

                    // ✅ 로그아웃 후 리디렉션
                    response.sendRedirect("/login");
                })
        );
        return http.build();
    }
}
