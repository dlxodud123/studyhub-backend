package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.response.MemberResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1");
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2");

        memberService.registerMember(signupRequestDto1);
        memberService.registerMember(signupRequestDto2);
    }

    @Test
    public void signup() {
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3");

        memberService.registerMember(signupRequestDto);
        List<Member> findAll = memberRepository.findAll();

        assertThat(findAll.size()).isEqualTo(3);

        Member lastMember = findAll.get(findAll.size() - 1);

        // username, email, password 검증
        assertThat(lastMember.getUsername()).isEqualTo("user3");
        assertThat(lastMember.getEmail()).isEqualTo("email3");
        assertThat(passwordEncoder.matches("password3", lastMember.getPassword())).isTrue();
        assertThat(lastMember.getProvider()).isEqualTo(ProviderType.LOCAL);
    }

    @Test
    public void login() {
        String username = "user2";
        String password = "password2";
        String email = "email2";

        Member findMember = memberRepository.findByUsername(username).get();

        assertThat(findMember.getUsername()).isEqualTo(username);
        assertThat(passwordEncoder.matches(password, findMember.getPassword())).isTrue();
        assertThat(findMember.getEmail()).isEqualTo(email);
    }

    @Test
    public void searchOneById() {

        Member findMember = memberService.registerMember(new SignupRequestDto("user3", "password3", "email3"));

        MemberResponseDto responseDto = memberService.getMyInfo(findMember.getId());

        assertThat(responseDto.getUsername()).isEqualTo("user3");
        assertThat(responseDto.getEmail()).isEqualTo("email3");
    }

    @Test
    public void searchOneByUsername() {
        Member findMember = memberService.registerMember(new SignupRequestDto("user3", "password3", "email3"));

        Member searchMember = memberService.findByUsernameOrThrow(findMember.getUsername());

        assertThat(searchMember.getUsername()).isEqualTo("user3");
        assertThat(searchMember.getEmail()).isEqualTo("email3");
        assertThat(passwordEncoder.matches("password3", searchMember.getPassword())).isTrue();
    }

    @Test
    public void update() {

    }

    @Test
    public void delete() {

    }

    @Test
    public void validateEmail() {

    }
}
