package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.response.MemberResponseDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateEmailException;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateUsernameException;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 회원가입
    public Member registerMember(SignupRequestDto signupRequestDto){
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        if (memberRepository.existsByUsername(signupRequestDto.getUsername())) {
            throw new DuplicateUsernameException("이미 사용중인 username입니다.");
        }
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateEmailException("이미 사용중인 email입니다.");
        }

        return memberRepository.save(new Member(
                signupRequestDto.getUsername(),
                encodedPassword,
                signupRequestDto.getEmail(),
                signupRequestDto.getProvider(),
                Role.USER
        ));
    }

    // 회원 탈퇴
    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        memberRepository.delete(member);
    }

    // 회원 정보 수정
    public void updateMember(UpdateRequestDto updateRequestDto, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        String encodedPassword = passwordEncoder.encode(updateRequestDto.getPassword());
        member.updateMember(encodedPassword, updateRequestDto.getEmail());
    }

    // username 찾기
    public String findByUsernameByEmail(String email) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        return findMember.getUsername();
    }

    // password 찾기
    public String findByPasswordByUsername(String username) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 임시 비밀번호 생성 (UUID 앞 8자리)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        // 암호화 후 member 객체에 set
        findMember.setRandomPassword(passwordEncoder.encode(tempPassword));
        // DB 업데이트
        memberRepository.save(findMember);

        return tempPassword; // 사용자에게 보여줄 임시 비밀번호
    }

    // email 찾기
    public String findByEmailByUsernameAndPassword(String username, String password) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 입력한 비밀번호와 암호화된 비밀번호 비교
        if (passwordEncoder.matches(password, findMember.getPassword())) {
            return findMember.getEmail();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    // test 전용
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo(Long id){
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return new MemberResponseDto(findMember.getUsername(), findMember.getEmail());
    }
    public Member findByUsername(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
    }
}
