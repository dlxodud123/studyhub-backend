package com.taeyoung.studyhub.studyhub_backend.exception;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateEmailException;
import com.taeyoung.studyhub.studyhub_backend.global.exception.DuplicateUsernameException;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
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

    private Member member1;
    private Member member2;

    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.LOCAL);
        member1 = memberService.registerMember(signupRequestDto1);
        member2 = memberService.registerMember(signupRequestDto2);
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

    @Test
    public void deleteMemberException() {
        // when, then
        assertThatThrownBy(() ->
                memberService.deleteMember(123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void updateMemberException() {
        // given
        UpdateRequestDto updateRequestDto = new UpdateRequestDto("updatePassword", "updateEmail");

        // when, then
        assertThatThrownBy(() ->
                memberService.updateMember(updateRequestDto, 123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void findUsernameException() {
        // when, then
        assertThatThrownBy(() ->
                memberService.findByUsernameByEmail("email")
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void findPasswordException() {
        // when, then
        assertThatThrownBy(() ->
                memberService.findByPasswordByUsername("user")
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void findEmailException() {
        // when, then
        assertThatThrownBy(() ->
                memberService.findByEmailByUsernameAndPassword("user", "password1")
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");

        assertThatThrownBy(() ->
                memberService.findByEmailByUsernameAndPassword("user1", "password")
        )
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
