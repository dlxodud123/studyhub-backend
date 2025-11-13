package com.taeyoung.studyhub.studyhub_backend.jwt;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String jwtCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtCookie != null) {
            try {
                Claims claim = JwtUtil.extractToken(jwtCookie);

                var arr = claim.get("authorities").toString().split(",");
                var authorities = Arrays.stream(arr)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                Long id;
                try {
                    id = Long.valueOf(claim.get("id").toString());
                } catch (NumberFormatException e) {
                    id = (long) Double.parseDouble(claim.get("id").toString());
                }

                var customUser = new CustomUser(
                        id,
                        claim.get("username").toString(),
                        "none",
                        authorities
                );

                var authToken = new UsernamePasswordAuthenticationToken(customUser, "", authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                // 토큰 검증 실패 시 인증 정보 비움
                SecurityContextHolder.clearContext();
            }
        } else {
            // JWT 쿠키 없으면 인증 정보 비움
            SecurityContextHolder.clearContext();

            // DELETE 경로라면 401 상태 코드 보내고 종료
            if (request.getRequestURI().startsWith("/delete")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // 다음 필터로 요청 계속 전달
        filterChain.doFilter(request, response);
    }
}
