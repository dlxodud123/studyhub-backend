package com.taeyoung.studyhub.studyhub_backend.auth.oauth;

import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleLoginService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // email 중복 체크
    public boolean checkEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    // username 중복 체크
    public boolean checkUsernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }
}
