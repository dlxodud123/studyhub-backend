package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @BeforeEach
    public void before() {
        Member member1 = new Member("user1", "password1", "email1", Role.USER, ProviderType.LOCAL);
        Member member2 = new Member("user2", "password2", "email2", Role.USER, ProviderType.GOOGLE);

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    public void signup() {
        Member member3 = new Member("user3", "password3", "email3", Role.USER, ProviderType.LOCAL);

        memberRepository.save(member3);
        List<Member> findAll = memberRepository.findAll();

        System.out.println("findAll = " + findAll);

        assertThat(findAll.size()).isEqualTo(3);
    }

//    @Test
//    @Rollback(value = false)
//    public void signupTest() {
//        SignupRequestDto signupRequestDto = new SignupRequestDto("asdf", "asdf", "asdf@asdf");
//
//        memberService.registerMember(signupRequestDto);
//
//        boolean existsByUsername = memberRepository.existsByUsername("asdf");
//        boolean existsByEmail = memberRepository.existsByEmail("asdf@asdf");
//        assertThat(existsByUsername).isTrue();
//        assertThat(existsByEmail).isTrue();
//    }

}
