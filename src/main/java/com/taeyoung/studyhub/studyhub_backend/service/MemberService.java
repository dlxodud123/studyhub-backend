package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 회원가입
    public String registerMember(SignupRequestDto signupRequestDto){
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        memberRepository.save(new Member(
                signupRequestDto.getUsername(),
                encodedPassword,
                signupRequestDto.getEmail(),
                Role.USER
        ));

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
