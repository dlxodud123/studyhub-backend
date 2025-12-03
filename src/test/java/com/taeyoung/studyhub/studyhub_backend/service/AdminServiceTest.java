package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminStudiesResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdminServiceTest {

    @Autowired private StudyRepository studyRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AdminService adminService;
    @Autowired private MemberService memberService;
    @Autowired private StudyService studyService;

    @Autowired private EntityManager em;

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
    public void getDashboard() {
        // when
        AdminDashboardResponseDto dashboardData = adminService.getDashboardData();

        // then
        assertThat(dashboardData.getMemberCount()).isEqualTo(4L);
        assertThat(dashboardData.getStudyCount()).isEqualTo(4L);

        assertThat(dashboardData.getRecentMembers().get(2).getId()).isEqualTo(member2.getId());
        assertThat(dashboardData.getRecentMembers().get(2).getName()).isEqualTo("user2");
        assertThat(dashboardData.getRecentMembers().get(2).getEmail()).isEqualTo("email2");
        assertThat(dashboardData.getRecentMembers().get(1).getId()).isEqualTo(member3.getId());
        assertThat(dashboardData.getRecentMembers().get(1).getName()).isEqualTo("user3");
        assertThat(dashboardData.getRecentMembers().get(1).getEmail()).isEqualTo("email3");
        assertThat(dashboardData.getRecentMembers().get(0).getId()).isEqualTo(member4.getId());
        assertThat(dashboardData.getRecentMembers().get(0).getName()).isEqualTo("user4");
        assertThat(dashboardData.getRecentMembers().get(0).getEmail()).isEqualTo("email4");

        assertThat(dashboardData.getRecentStudies().get(2).getId()).isEqualTo(study2.getId());
        assertThat(dashboardData.getRecentStudies().get(2).getName()).isEqualTo("testTitle2");
        assertThat(dashboardData.getRecentStudies().get(2).getCategory()).isEqualTo("testCategory2");
        assertThat(dashboardData.getRecentStudies().get(1).getId()).isEqualTo(study3.getId());
        assertThat(dashboardData.getRecentStudies().get(1).getName()).isEqualTo("testTitle3");
        assertThat(dashboardData.getRecentStudies().get(1).getCategory()).isEqualTo("testCategory3");
        assertThat(dashboardData.getRecentStudies().get(0).getId()).isEqualTo(study4.getId());
        assertThat(dashboardData.getRecentStudies().get(0).getName()).isEqualTo("testTitle4");
        assertThat(dashboardData.getRecentStudies().get(0).getCategory()).isEqualTo("testCategory4");
    }

    @Test
    public void getMembers() {
        // when
        Page<AdminMembersResponseDto> members = adminService.getMembers(0, 9);

        // then
        assertThat(members.getTotalElements()).isEqualTo(4L);

        assertThat(members.getContent().get(0).getId()).isEqualTo(member1.getId());
        assertThat(members.getContent().get(0).getName()).isEqualTo("user1");
        assertThat(members.getContent().get(0).getEmail()).isEqualTo("email1");
        assertThat(members.getContent().get(1).getId()).isEqualTo(member2.getId());
        assertThat(members.getContent().get(1).getName()).isEqualTo("user2");
        assertThat(members.getContent().get(1).getEmail()).isEqualTo("email2");
        assertThat(members.getContent().get(2).getId()).isEqualTo(member3.getId());
        assertThat(members.getContent().get(2).getName()).isEqualTo("user3");
        assertThat(members.getContent().get(2).getEmail()).isEqualTo("email3");
        assertThat(members.getContent().get(3).getId()).isEqualTo(member4.getId());
        assertThat(members.getContent().get(3).getName()).isEqualTo("user4");
        assertThat(members.getContent().get(3).getEmail()).isEqualTo("email4");
    }

    @Test
    public void memberDelete() {
        em.flush();
        em.clear();

        // when
        adminService.deleteMember(member4.getId());

        Page<AdminMembersResponseDto> members = adminService.getMembers(0, 9);

        // then
        assertThat(members.getTotalElements()).isEqualTo(3L);

        assertThat(members.getContent().get(0).getId()).isEqualTo(member1.getId());
        assertThat(members.getContent().get(0).getName()).isEqualTo("user1");
        assertThat(members.getContent().get(0).getEmail()).isEqualTo("email1");
        assertThat(members.getContent().get(1).getId()).isEqualTo(member2.getId());
        assertThat(members.getContent().get(1).getName()).isEqualTo("user2");
        assertThat(members.getContent().get(1).getEmail()).isEqualTo("email2");
        assertThat(members.getContent().get(2).getId()).isEqualTo(member3.getId());
        assertThat(members.getContent().get(2).getName()).isEqualTo("user3");
        assertThat(members.getContent().get(2).getEmail()).isEqualTo("email3");
    }
                                                                                           
    @Test
    public void memberChangeRole() {
        // when
        Member member = memberRepository.findById(member1.getId()).orElseThrow();
        em.flush();
        em.clear();
        adminService.changeRole(member1.getId(), "ADMIN");
        Member updatedMember = memberRepository.findById(member1.getId()).orElseThrow();

        // then
        assertThat(member.getRole()).isEqualTo(Role.USER);
        assertThat(updatedMember.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    public void getStudies() {
        // when
        Page<AdminStudiesResponseDto> studies = adminService.getStudies(0, 9);

        // then
        assertThat(studies.getTotalElements()).isEqualTo(4L);
                                                                        
        assertThat(studies.getContent().get(0).getId()).isEqualTo(study1.getId());
        assertThat(studies.getContent().get(0).getName()).isEqualTo("testTitle1");
        assertThat(studies.getContent().get(0).getCategory()).isEqualTo("testCategory1");
        assertThat(studies.getContent().get(1).getId()).isEqualTo(study2.getId());
        assertThat(studies.getContent().get(1).getName()).isEqualTo("testTitle2");
        assertThat(studies.getContent().get(1).getCategory()).isEqualTo("testCategory2");
        assertThat(studies.getContent().get(2).getId()).isEqualTo(study3.getId());
        assertThat(studies.getContent().get(2).getName()).isEqualTo("testTitle3");
        assertThat(studies.getContent().get(2).getCategory()).isEqualTo("testCategory3");
        assertThat(studies.getContent().get(3).getId()).isEqualTo(study4.getId());
        assertThat(studies.getContent().get(3).getName()).isEqualTo("testTitle4");
        assertThat(studies.getContent().get(3).getCategory()).isEqualTo("testCategory4");
    }

    @Test
    public void studyDelete() {
        em.flush();
        em.clear();

        // when
        adminService.deleteStudy(study4.getId());

        Page<AdminStudiesResponseDto> studies = adminService.getStudies(0, 9);

        // then
        assertThat(studies.getTotalElements()).isEqualTo(3L);

        assertThat(studies.getContent().get(0).getId()).isEqualTo(study1.getId());
        assertThat(studies.getContent().get(0).getName()).isEqualTo("testTitle1");
        assertThat(studies.getContent().get(0).getCategory()).isEqualTo("testCategory1");
        assertThat(studies.getContent().get(1).getId()).isEqualTo(study2.getId());
        assertThat(studies.getContent().get(1).getName()).isEqualTo("testTitle2");
        assertThat(studies.getContent().get(1).getCategory()).isEqualTo("testCategory2");
        assertThat(studies.getContent().get(2).getId()).isEqualTo(study3.getId());
        assertThat(studies.getContent().get(2).getName()).isEqualTo("testTitle3");
        assertThat(studies.getContent().get(2).getCategory()).isEqualTo("testCategory3");
    }
}