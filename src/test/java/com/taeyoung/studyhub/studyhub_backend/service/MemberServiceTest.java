package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void before() {
        Member member1 = new Member("user1", "password1", "email1", Role.USER, ProviderType.LOCAL);
        Member member2 = new Member("user2", "password2", "email2", Role.USER, ProviderType.GOOGLE);

        memberRepository.save(member1);
        memberRepository.save(member2);
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
    }

    @Test
    public void login() {
        String username = "user3";
        String password = "password3";
//        Optional<Member> findMember = memberRepository.findByUsername("user1");
    }
}
