package com.taeyoung.studyhub.studyhub_backend.exception;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import com.taeyoung.studyhub.studyhub_backend.service.AdminService;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import com.taeyoung.studyhub.studyhub_backend.service.StudyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdminExceptionTest {

    @Autowired private StudyRepository studyRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AdminService adminService;
    @Autowired private MemberService memberService;
    @Autowired private StudyService studyService;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;
    private Study study1;
    private Study study2;
    private Study study3;
    private Study study4;

    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto3 = new SignupRequestDto("user3", "password3", "email3", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto4 = new SignupRequestDto("user4", "password4", "email4", ProviderType.LOCAL);
        member1 = memberService.registerMember(signupRequestDto1);
        member2 = memberService.registerMember(signupRequestDto2);
        member3 = memberService.registerMember(signupRequestDto3);
        member4 = memberService.registerMember(signupRequestDto4);
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        Category category2 = categoryRepository.save(new Category("testCategory2"));
        Category category3 = categoryRepository.save(new Category("testCategory3"));
        Category category4 = categoryRepository.save(new Category("testCategory4"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle2", "testContent2", category2.getId(), List.of("testTag3", "testTag4"));
        StudyCreateRequestDto dto3 = new StudyCreateRequestDto("testTitle3", "testContent3", category3.getId(), List.of("testTag5", "testTag6"));
        StudyCreateRequestDto dto4 = new StudyCreateRequestDto("testTitle4", "testContent4", category4.getId(), List.of("testTag7", "testTag8"));
        study1 = studyService.createStudy(dto1, member1.getId());
        study2 = studyService.createStudy(dto2, member2.getId());
        study3 = studyService.createStudy(dto3, member3.getId());
        study4 = studyService.createStudy(dto4, member4.getId());
    }

    @Test
    public void deleteMember() {
        // when, then
        assertThatThrownBy(() ->
                adminService.deleteMember(123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void changeRole() {
        // when, then
        assertThatThrownBy(() ->
                adminService.changeRole(123L, "USER")
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    public void deleteStudy() {
        // when, then
        assertThatThrownBy(() ->
                adminService.deleteStudy(123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");
    }
}
