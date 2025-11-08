package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 
    @PutMapping("/api/members/login")
    public String loginMember(){
        memberService.login();
        return "login";
    }
    // 로그인 2
    @PutMapping("/api/members/login2")
    public String loginMember2(){
        memberService.login();
        return "login2";
    }
    // 로그인 2
    @PutMapping("/api/members/login3")
    public String loginMember3(){
        memberService.login();
        return "login3";
    }

    // 회원가입
    @PutMapping("/api/members/signup")
    public String registerMember(){
        memberService.registerMember();
        return "signup";
    }

    // 회원 정보 조회
    @GetMapping("/api/members/me")
    public String getMyInfo(){
        memberService.getMyInfo();
        return "me";
    }

    // 회원 정보 수정
    @PutMapping("/api/members/me")
    public String updateMyInfo(){
        memberService.updateMember();
        return "update";
    }

    // 회원 탈퇴
    @DeleteMapping("/api/members/me")
    public String deleteMyAccount(){
        memberService.deleteMember();
        return "delete";
    }
}
