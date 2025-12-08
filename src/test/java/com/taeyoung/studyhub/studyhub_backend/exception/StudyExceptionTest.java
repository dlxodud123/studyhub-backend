package com.taeyoung.studyhub.studyhub_backend.exception;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Comment;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.IdNotMatchException;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CommentRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
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
public class StudyExceptionTest {

    @Autowired private StudyRepository studyRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private StudyService studyService;
    @Autowired private MemberService memberService;

    private Member member1;
    private Member member2;
    private Study study1;
    private Study study2;
    private Comment comment1;
    private Comment comment2;

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
        comment1 = studyService.createComment("testComment1", member1.getId(), study1.getId());
        comment2 = studyService.createComment("testComment2", member1.getId(), study1.getId());
    }

    @Test
    public void createStudyException() {
        // given
        StudyCreateRequestDto dto = new StudyCreateRequestDto("testTitle3", "testContent3", 123L, List.of("testTag5", "testTag6"));

        // when, then
        assertThatThrownBy(() ->
                studyService.createStudy(dto, 123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");

        assertThatThrownBy(() ->
                studyService.createStudy(dto, member1.getId())
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("카테고리가 존재하지 않습니다.");
    }

    @Test
    public void findStudyDetailById() {
        // when, then
        assertThatThrownBy(() ->
                studyService.findStudyDetailById(123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");
    }

    @Test
    public void findStudyEditById() {
        // when, then
        assertThatThrownBy(() ->
                studyService.findStudyEditById(123L, member1.getId())
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");

        assertThatThrownBy(() ->
                studyService.findStudyEditById(study1.getId(), 123L)
        )
                .isInstanceOf(IdNotMatchException.class)
                .hasMessage("작성자만 수정할 수 있습니다.");
    }

    @Test
    public void editStudyById() {
        // given
        StudyEditRequestDto dto = new StudyEditRequestDto("testTitle3", "testContent3");

        // when, then
        assertThatThrownBy(() ->
                studyService.editStudyById(dto, 123L, member1.getId())
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");

        assertThatThrownBy(() ->
                studyService.editStudyById(dto, study1.getId(), 123L)
        )
                .isInstanceOf(IdNotMatchException.class)
                .hasMessage("작성자만 수정할 수 있습니다.");
    }

    @Test
    public void deleteStudyById() {
        // when, then
        assertThatThrownBy(() ->
                studyService.deleteStudyById(123L, member1.getId())
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");

        assertThatThrownBy(() ->
                studyService.deleteStudyById(study1.getId(), 123L)
        )
                .isInstanceOf(IdNotMatchException.class)
                .hasMessage("작성자만 삭제할 수 있습니다.");
    }

    @Test
    public void createComment() {
        // when, then
        assertThatThrownBy(() ->
                studyService.createComment("comment3", 123L, study1.getId())
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("회원이 존재하지 않습니다.");

        assertThatThrownBy(() ->
                studyService.createComment("comment3", member1.getId(), 123L)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("스터디가 존재하지 않습니다.");
    }

    
}
