package com.taeyoung.studyhub.studyhub_backend.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taeyoung.studyhub.studyhub_backend.auth.jwt.JwtUtil;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.EmailNotMatchException;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // 1. username으로 회원 조회
            Member member = memberRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("username 또는 password가 일치하지 않습니다."));

            // 2. email 검증
            if (!member.getEmail().equals(loginRequest.getEmail())) {
                throw new EmailNotMatchException("email이 일치하지 않습니다.");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // JWT 생성
        String jwt = JwtUtil.createToken(authResult);

        // 쿠키에 JWT 저장
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String message;
        if (failed instanceof BadCredentialsException) {
            message = "username 또는 password가 일치하지 않습니다.";
        } else if (failed instanceof EmailNotMatchException) {
            message = failed.getMessage();
        } else {
            message = "인증 실패";
        }
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
