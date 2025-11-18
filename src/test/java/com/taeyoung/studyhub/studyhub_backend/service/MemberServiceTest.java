package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
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
import java.util.UUID;

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
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.LOCAL);
        memberService.registerMember(signupRequestDto1);
        memberService.registerMember(signupRequestDto2);
    }

    @Test
    public void signup() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        memberService.registerMember(signupRequestDto);
        List<Member> findAll = memberRepository.findAll();
        Member lastMember = findAll.get(findAll.size() - 1);

        // then
        assertThat(findAll.size()).isEqualTo(3);
        assertThat(lastMember.getUsername()).isEqualTo("user3");
        assertThat(lastMember.getEmail()).isEqualTo("email3");
        assertThat(passwordEncoder.matches("password3", lastMember.getPassword())).isTrue();
        assertThat(lastMember.getProvider()).isEqualTo(ProviderType.LOCAL);
    }

    @Test
    public void login() {
        // given
        String username = "user2";
        String password = "password2";
        String email = "email2";

        // when
        Member findMember = memberRepository.findByUsername(username).get();

        // then
        assertThat(findMember.getUsername()).isEqualTo(username);
        assertThat(passwordEncoder.matches(password, findMember.getPassword())).isTrue();
        assertThat(findMember.getEmail()).isEqualTo(email);
    }

    @Test
    public void searchOneById() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        Member findMember = memberService.registerMember(signupRequestDto);
        MemberResponseDto responseDto = memberService.getMyInfo(findMember.getId());

        // then
        assertThat(responseDto.getUsername()).isEqualTo("user3");
        assertThat(responseDto.getEmail()).isEqualTo("email3");
    }

    @Test
    public void searchOneByUsername() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        Member findMember = memberService.registerMember(signupRequestDto);
        Member searchMember = memberService.findByUsername(findMember.getUsername());

        // then
        assertThat(searchMember.getUsername()).isEqualTo("user3");
        assertThat(searchMember.getEmail()).isEqualTo("email3");
        assertThat(passwordEncoder.matches("password3", searchMember.getPassword())).isTrue();
    }

    @Test
    public void update() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        Member findMember = memberService.registerMember(signupRequestDto);
        memberService.updateMember(new UpdateRequestDto("changePassword", "changeEmail"), findMember.getId());
        MemberResponseDto dto = memberService.getMyInfo(findMember.getId());
        Member findMemberByUsername = memberService.findByUsername(dto.getUsername());

        // then
        assertThat(dto.getEmail()).isEqualTo("changeEmail");
        assertThat(passwordEncoder.matches("changePassword", findMemberByUsername.getPassword())).isTrue();
    }

    @Test
    public void delete() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        Member findMember = memberService.registerMember(signupRequestDto);
        memberService.deleteMember(findMember.getId());

        List<Member> findAll = memberRepository.findAll();
        Member lastMember = findAll.get(findAll.size() - 1);

        // then
        assertThat(findAll.size()).isEqualTo(2);
        assertThat(lastMember.getUsername()).isEqualTo("user2");
        assertThat(passwordEncoder.matches("password2", lastMember.getPassword())).isTrue();
        assertThat(lastMember.getEmail()).isEqualTo("email2");
    }


    // username 찾기
    @Test
    public void findByUsernameByEmail() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);

        // when
        Member findMember = memberService.registerMember(signupRequestDto);
        String findUsername = memberService.findByUsernameByEmail(findMember.getEmail());

        // then
        assertThat(findMember.getUsername()).isEqualTo(findUsername);
    }

    // password 찾기
    @Test
    public void findByPasswordByUsername() {
        // when
        String password = memberService.findByPasswordByUsername("user1");

        // then
        assertThat(password).isNotNull();
    }

    // email 찾기
    @Test
    public void findByEmailByPassword() {
        // when
        String findEmail = memberService.findByEmailByUsernameAndPassword("user1", "password1");

        // then
        assertThat(findEmail).isEqualTo("email1");
    }
}
