package com.taeyoung.studyhub.studyhub_backend.service;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    // 로그인
    public String login(){

        return "login";
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
