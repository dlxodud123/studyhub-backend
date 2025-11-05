package com.taeyoung.studyhub.studyhub_backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    // 로그인 
    @PutMapping("/api/members/login")
    public String loginMember(){
        
        return "login";
    }

    // 회원가입
    @PutMapping("/api/members/signup")
    public String registerMember(){

        return "signup";
    }

    // 회원 정보 조회
    @GetMapping("/api/members/me")
    public String getMyInfo(){

        return "me";
    }

    // 회원 정보 수정
    @PutMapping("/api/members/me")
    public String updateMyInfo(){

        return "update";
    }

    // 회원 탈퇴
    @DeleteMapping("/api/members/me")
    public String deleteMyAccount(){

        return "delete";
    }
}
