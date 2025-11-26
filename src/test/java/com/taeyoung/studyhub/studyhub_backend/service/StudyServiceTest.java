package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
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
    @Autowired private CategoryRepository categoryRepository;

    private Member member1;
    private Member member2;
    private Study study1;
    private Study study2;


    @BeforeEach
    public void before() {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("user1", "password1", "email1", ProviderType.LOCAL);
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("user2", "password2", "email2", ProviderType.GOOGLE);
        member1 = memberService.registerMember(signupRequestDto1);
        member2 = memberService.registerMember(signupRequestDto2);
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        Category category2 = categoryRepository.save(new Category("testCategory2"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle2", "testContent2", category2.getId(), List.of("testTag3", "testTag4"));
        study1 = studyService.createStudy(dto1, member1.getId());
        study2 = studyService.createStudy(dto2, member2.getId());
    }

    @Test
    public void getStudyList() {
        // when
        List<StudyListResponseDto> studyList = studyService.getStudyList();

        // then
        assertThat(studyList.size()).isEqualTo(2);
        assertThat(studyList.get(0).getTitle()).isEqualTo("testTitle1");
        assertThat(studyList.get(0).getContent()).isEqualTo("testContent1");
        assertThat(studyList.get(0).getCreatedBy()).isEqualTo("user1");
        // 추가: 카테고리 & 태그 검증
        assertThat(studyList.get(0).getCategoryName()).isEqualTo("testCategory1");
        assertThat(studyList.get(0).getTagNames()).containsExactlyInAnyOrder("testTag1", "testTag2");

        assertThat(studyList.get(1).getTitle()).isEqualTo("testTitle2");
        assertThat(studyList.get(1).getContent()).isEqualTo("testContent2");
        assertThat(studyList.get(1).getCreatedBy()).isEqualTo("user2");
        // 추가: 카테고리 & 태그 검증
        assertThat(studyList.get(1).getCategoryName()).isEqualTo("testCategory2");
        assertThat(studyList.get(1).getTagNames()).containsExactlyInAnyOrder("testTag3", "testTag4");
    }

    @Test
    public void findDetailStudy() {
        // when
        StudyDetailResponseDto detailDto = studyService.findStudyDetailById(study1.getId());

        // then
        assertThat(detailDto.getTitle()).isEqualTo("testTitle1");
        assertThat(detailDto.getContent()).isEqualTo("testContent1");
        assertThat(detailDto.getCreatedBy()).isEqualTo("user1");
        assertThat(detailDto.getCategoryName()).isEqualTo("testCategory1");
        assertThat(detailDto.getTagNames()).containsExactlyInAnyOrder("testTag1", "testTag2");
    }

    @Test
    public void findEditStudy() {
        // when
        StudyEditResponseDto editDto = studyService.findStudyEditById(study1.getId(), member1.getId());

        // then
        assertThat(editDto.getTitle()).isEqualTo("testTitle1");
        assertThat(editDto.getContent()).isEqualTo("testContent1");
        assertThat(editDto.getCreatedBy()).isEqualTo("user1");
        assertThatThrownBy(() ->
                studyService.findStudyEditById(study1.getId(), member2.getId())
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("작성자만 수정할 수 있습니다.");

    }

    @Test
    public void editStudy() {
        // given
        StudyEditRequestDto editRequestDto = new StudyEditRequestDto("editTitle", "editContent");

        // when
        studyService.editStudyById(editRequestDto, study1.getId());

        // then
        assertThat(study1.getTitle()).isEqualTo("editTitle");
        assertThat(study1.getContent()).isEqualTo("editContent");
    }

    @Test
    public void deleteStudy() {
        System.out.println("===== BEFORE CHECK =====");
        System.out.println("study1 ID = " + study1.getId() + ", writer = " + study1.getMember().getUsername());
        System.out.println("study2 ID = " + study2.getId() + ", writer = " + study2.getMember().getUsername());
        System.out.println("member1 ID = " + member1.getId() + ", username = " + member1.getUsername());
        System.out.println("member2 ID = " + member2.getId() + ", username = " + member2.getUsername());
        System.out.println("========================");

        // when
//        studyService.deleteStudyById(study1.getId(), member1.getId());
        List<StudyListResponseDto> studyList = studyService.getStudyList();

        // then
        assertThat(studyList.size()).isEqualTo(1);
        assertThat(studyList.get(0).getTitle()).isEqualTo("testTitle2");
        assertThat(studyList.get(0).getContent()).isEqualTo("testContent2");
        assertThat(studyList.get(0).getCreatedBy()).isEqualTo("user2");
        assertThatThrownBy(() ->
                studyService.deleteStudyById(study2.getId(), member1.getId())
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("작성자만 삭제할 수 있습니다.");
    }
}
