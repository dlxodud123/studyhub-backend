package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

        if (memberRepository.existsByUsername(signupRequestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        memberRepository.save(new Member(
                signupRequestDto.getUsername(),
                encodedPassword,
                signupRequestDto.getEmail(),
                Role.USER,
                ProviderType.LOCAL
        ));

        return "signup";
    }

    // 회원 정보 조회
//    public String getMyInfo(){
//        return "me";
//    }

    // 회원 정보 수정
//    public String updateMember(){
//
//        return "update";
//    }

    // 회원 탈퇴
    public void deleteMember(Long id){
        try {
            memberRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("이미 삭제되었거나 존재하지 않는 사용자입니다.");
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("연관된 데이터가 있어 회원을 삭제할 수 없습니다.");
        }
    }
}
