package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;

    // 로그인
    public ResponseEntity<String> login(LoginRequestDto loginRequestDto){
        System.out.println("username : " + loginRequestDto.getUsername());
        System.out.println("password : " + loginRequestDto.getPassword());
        System.out.println("email : " + loginRequestDto.getEmail());

        String token = jwtUtil.generateToken(loginRequestDto.getUsername());

        System.out.println("token : " + token);

        return ResponseEntity.ok(token);
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
