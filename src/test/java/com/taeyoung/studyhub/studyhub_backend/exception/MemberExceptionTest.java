package com.taeyoung.studyhub.studyhub_backend.exception;

import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateEmailException;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateUsernameException;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberExceptionTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.LOCAL);
        memberService.registerMember(signupRequestDto1);
        memberService.registerMember(signupRequestDto2);
    }

    @Test
    public void signupException() {
        // given
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user", "password1", "email1", ProviderType.LOCAL);

        // when, then
        assertThatThrownBy(() ->
            memberService.registerMember(signupRequestDto1)
        )
            .isInstanceOf(DuplicateUsernameException.class)
            .hasMessage("이미 사용중인 username입니다.");

        assertThatThrownBy(() ->
                memberService.registerMember(signupRequestDto2)
        )
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("이미 사용중인 email입니다.");
    }
}
