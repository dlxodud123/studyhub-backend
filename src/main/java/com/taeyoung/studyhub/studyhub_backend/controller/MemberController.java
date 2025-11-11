package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 
    @PutMapping("/api/members/login")
    public String loginMember(@RequestBody LoginRequestDto loginRequestDto){
        memberService.login(loginRequestDto);
        return "login";
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
