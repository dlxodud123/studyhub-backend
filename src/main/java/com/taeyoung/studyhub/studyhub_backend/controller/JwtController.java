package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.security.JwtUtil2;
import com.taeyoung.studyhub.studyhub_backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login/jwt")
    public String jwtLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        // Authentication 객체를 생성하고 SecurityContext에 적용
        var authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Authentication는 스레드 로컬을 사용하므로 각 요청마다 독립적이다.
        // Authentication에 auth를 createToken메서드에 보냄
        var jwt = JwtUtil2.createToken(SecurityContextHolder.getContext().getAuthentication());

        var cookie = new Cookie("jwt", jwt);
        //  JWT만들었을때의 기간이랑 같게
        cookie.setMaxAge(100);
        //  쿠키를 자바스크립트로 조작 못하게
        cookie.setHttpOnly(true);
        //  쿠키가 전송될 URL
        cookie.setPath("/");
        //  브라우저에 저장
        response.addCookie(cookie);

        return jwt;
    }

    @GetMapping("/my-page/jwt")
    public String myPage(Authentication auth){
        // SecurityContext에서 Authentication 객체를 찾아서 가져옴
        System.out.println(auth);
        System.out.println(auth.getName());
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("일반유저")));

//        if (auth != null && auth.isAuthenticated()){
//            return "mypage.html";
//        } else {
//            return "redirect:/";
//        }
        return "mypage.html";
    }
}
