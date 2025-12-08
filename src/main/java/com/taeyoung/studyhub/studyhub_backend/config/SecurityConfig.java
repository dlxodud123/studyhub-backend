package com.taeyoung.studyhub.studyhub_backend.config;

import com.taeyoung.studyhub.studyhub_backend.auth.jwt.JwtFilter;
import com.taeyoung.studyhub.studyhub_backend.auth.login.CustomAuthenticationFilter;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, MemberRepository memberRepository) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/my-page/**", "/modify/**", "/study/edit/**", "/api/studies/delete/**"
//                        , "/study/create", "/study/edit/**", "/study/delete/**",
                ).authenticated()
                // admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        );
        // auth 없이 지정된 경로 접속 시 /login으로 이동
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    // 1) API 요청인지 판별
                    String ajaxHeader = request.getHeader("X-Requested-With");
                    if ("XMLHttpRequest".equals(ajaxHeader)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json; charset=UTF-8");
                        response.getWriter().write("{\"message\": \"UNAUTHORIZED\"}");
                        return;
                    }

                    // 2) 그 외 페이지 요청이면 login으로 redirect
                    response.sendRedirect("/login");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().write(
                        "<script>" +
                                "alert('관리자만 접근 가능합니다.');" +
                                "location.href = '/';" +
                                "</script>"
                    );
                })
        );

        // CustomAuthenticationFilter 등록
        CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authenticationManager, memberRepository);
        http.addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);

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
