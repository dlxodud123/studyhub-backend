package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudyServiceTest {

    @Autowired private StudyRepository studyRepository;
    @Autowired private StudyService studyService;
    @Autowired private MemberService memberService;

    private Member member1;
    private Member member2;

    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.GOOGLE);
        member1 = memberService.registerMember(signupRequestDto1);
        member2 = memberService.registerMember(signupRequestDto2);
    }

    @Test
    public void getStudyList() {
        // given
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1");
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle2", "testContent2");

        // when
        studyService.createStudy(dto1, member1.getId());
        studyService.createStudy(dto2, member2.getId());
        List<StudyListResponseDto> studyList = studyService.getStudyList();

        // then
        assertThat(studyList.size()).isEqualTo(2);
        assertThat(studyList.get(0).getTitle()).isEqualTo("testTitle1");
        assertThat(studyList.get(0).getContent()).isEqualTo("testContent1");
        assertThat(studyList.get(0).getCreatedBy()).isEqualTo("user1");
        assertThat(studyList.get(1).getTitle()).isEqualTo("testTitle2");
        assertThat(studyList.get(1).getContent()).isEqualTo("testContent2");
        assertThat(studyList.get(1).getCreatedBy()).isEqualTo("user2");
    }

    @Test
    public void createStudy() {
        // given

        // when

        // then

    }

    @Test
    public void findDetailStudy() {
        // given

        // when

        // then

    }

    @Test
    public void findEditStudy() {
        // given

        // when

        // then

    }

    @Test
    public void editStudy() {
        // given

        // when

        // then

    }

    @Test
    public void deleteStudy() {
        // given

        // when

        // then

    }
}
