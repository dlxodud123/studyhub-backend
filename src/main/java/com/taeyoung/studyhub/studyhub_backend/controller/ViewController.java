package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("jwt".equals(cookie.getName()) && jwtUtil.validateToken(cookie.getValue())) {
//                    // 이미 로그인 되어있으면 메인 페이지 등으로 리다이렉트
//                    return "redirect:/";
//                }
//            }
//        }
        // JWT 없거나 유효하지 않으면 로그인 페이지 보여주기
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(HttpServletRequest request) {
        return "signup";
    }
}