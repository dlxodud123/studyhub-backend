package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인
    public ResponseEntity<String> login(LoginRequestDto loginRequestDto){
        System.out.println("username : " + loginRequestDto.getUsername());
        System.out.println("password : " + loginRequestDto.getPassword());
        System.out.println("email : " + loginRequestDto.getEmail());


        String token = jwtUtil.generateToken(loginRequestDto.getUsername());

        return ResponseEntity.ok(token);
    }

    // 로그인2
    public String login2(LoginRequestDto loginRequestDto){
        // 인증용 토큰 객체 생성
        var authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "";
    }

    // 회원가입
    public String registerMember(){

        return "signup";
    }

    // 회원 정보 조회
    public String getMyInfo(){

        return "me";
    }

    // 회원 정보 수정
    public String updateMember(){

        return "update";
    }

    // 회원 탈퇴
    public String deleteMember(){

        return "delete";
    }
}
