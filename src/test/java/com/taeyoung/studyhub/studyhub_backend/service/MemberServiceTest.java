package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Test
    @Rollback(value = false)
    public void signupTest() {
        SignupRequestDto signupRequestDto = new SignupRequestDto("asdf", "asdf", "asdf@asdf");

        memberService.registerMember(signupRequestDto);

        boolean existsByUsername = memberRepository.existsByUsername("asdf");
        boolean existsByEmail = memberRepository.existsByEmail("asdf@asdf");
        assertThat(existsByUsername).isTrue();
        assertThat(existsByEmail).isTrue();
    }

    @Test
    public void deleteTest() {

    }

}
