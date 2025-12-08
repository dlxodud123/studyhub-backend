package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Comment;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.CommentListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CommentRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import jakarta.persistence.EntityManager;
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
public class StudyServiceTest {

    @Autowired private StudyRepository studyRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private StudyService studyService;
    @Autowired private MemberService memberService;

    @Autowired private EntityManager em;

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

    // study
    @Test
    public void getStudyList() {
        // when
        Page<StudyListResponseDto> studyList = studyService.getStudyList(0, 9, "title", "", null);

        // then
        assertThat(studyList).isNotNull();
        assertThat(studyList.getContent()).hasSize(2);

        StudyListResponseDto s2 = studyList.getContent().get(0);
        StudyListResponseDto s1 = studyList.getContent().get(1);

        // 📌 첫 번째 게시글 검증
        assertThat(s1.getId()).isNotNull();
        assertThat(s1.getTitle()).isEqualTo("testTitle1");
        assertThat(s1.getContent()).isEqualTo("testContent1");
        assertThat(s1.getCreatedBy()).isEqualTo("user1");
        // 카테고리 & 태그 검증
        assertThat(s1.getCategoryName()).isEqualTo("testCategory1");
        assertThat(s1.getTagNames()).containsExactlyInAnyOrder("testTag1", "testTag2");

        // 📌 두 번째 게시글 검증
        assertThat(s2.getId()).isNotNull();
        assertThat(s2.getTitle()).isEqualTo("testTitle2");
        assertThat(s2.getContent()).isEqualTo("testContent2");
        assertThat(s2.getCreatedBy()).isEqualTo("user2");
        // 카테고리 & 태그 검증
        assertThat(s2.getCategoryName()).isEqualTo("testCategory2");
        assertThat(s2.getTagNames()).containsExactlyInAnyOrder("testTag3", "testTag4");
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

    }
    @Test
    public void editStudy() {
        // given
        StudyEditRequestDto editRequestDto = new StudyEditRequestDto("editTitle", "editContent");

        // when
        studyService.editStudyById(editRequestDto, study1.getId(), member1.getId());

        // then
        assertThat(study1.getTitle()).isEqualTo("editTitle");
        assertThat(study1.getContent()).isEqualTo("editContent");
    }
    @Test
    public void deleteStudy() {
        em.flush();
        em.clear();

        // when
        studyService.deleteStudyById(study1.getId(), member1.getId());
        Page<StudyListResponseDto> studyList = studyService.getStudyList(0, 9, "title", "", null);

        // then
        // 📌 1) 전체 개수 검증
        assertThat(studyList).isNotNull();
        assertThat(studyList.getTotalElements()).isEqualTo(1);
        StudyListResponseDto remaining = studyList.getContent().get(0);

        // 📌 2) 남아 있는 게시글이 study2인지 검증
        assertThat(remaining.getId()).isEqualTo(study2.getId());
        assertThat(remaining.getTitle()).isEqualTo("testTitle2");
        assertThat(remaining.getContent()).isEqualTo("testContent2");
        assertThat(remaining.getCreatedBy()).isEqualTo("user2");
        assertThat(remaining.getCategoryName()).isEqualTo("testCategory2");
        assertThat(remaining.getTagNames()).containsExactlyInAnyOrder("testTag3", "testTag4");
    }

    // comment
    @Test
    public void getCommentList() {
        // when
        studyService.createComment("testComment3", member1.getId(), study1.getId());
        List<CommentListResponseDto> commentList = studyService.getCommentList(study1.getId());

        // then
        assertThat(commentList.get(0).getCreatedBy()).isEqualTo("user1");
        assertThat(commentList.get(0).getContent()).isEqualTo("testComment1");
        assertThat(commentList.get(1).getCreatedBy()).isEqualTo("user1");
        assertThat(commentList.get(1).getContent()).isEqualTo("testComment2");
        assertThat(commentList.get(2).getCreatedBy()).isEqualTo("user1");
        assertThat(commentList.get(2).getContent()).isEqualTo("testComment3");
    }

    // page
    @Test
    public void listPage() {
        // given
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        Category category2 = categoryRepository.save(new Category("testCategory2"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle2", "testContent2", category2.getId(), List.of("testTag3", "testTag4"));
        for (int i = 0; i < 4; i++) {
            studyService.createStudy(dto1, member1.getId());
            studyService.createStudy(dto2, member2.getId());
        }

        // when
        Page<StudyListResponseDto> studyList = studyService.getStudyList(0, 9, "title", "", null);

        // then
        assertThat(studyList.getSize()).isEqualTo(9);
        assertThat(studyList.getTotalPages()).isEqualTo(2);
        assertThat(studyList.getTotalElements()).isEqualTo(10);
    }

    // search(order)
    @Test
    public void searchOrder() {
        // given
        Category category = categoryRepository.save(new Category("testCategory3"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category.getId(), List.of("testTag1", "testTag2"));
        studyService.createStudy(dto1, member1.getId());

        // when
        Page<StudyListResponseDto> studyList = studyService.getStudyList(0, 9, "title", "", null);

        // then
        assertThat(studyList.getContent().get(0).getCategoryName()).isEqualTo("testCategory3");
        assertThat(studyList.getContent().get(1).getCategoryName()).isEqualTo("testCategory2");
        assertThat(studyList.getContent().get(2).getCategoryName()).isEqualTo("testCategory1");
    }
    // search(category)
    @Test
    public void searchCategory() {
        // given
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        Category category2 = categoryRepository.save(new Category("testCategory2"));
        Category category3 = categoryRepository.save(new Category("testCategory3"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent3", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle2", "testContent3", category2.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto3 = new StudyCreateRequestDto("testTitle3", "testContent3", category3.getId(), List.of("testTag1", "testTag2"));
        studyService.createStudy(dto1, member1.getId());
        studyService.createStudy(dto1, member1.getId());
        studyService.createStudy(dto2, member1.getId());
        studyService.createStudy(dto3, member1.getId());

        // when
        Page<StudyListResponseDto> studyList1 = studyService.getStudyList(0, 9, "title", "", category1.getId());
        Page<StudyListResponseDto> studyList2 = studyService.getStudyList(0, 9, "title", "", category2.getId());
        Page<StudyListResponseDto> studyList3 = studyService.getStudyList(0, 9, "title", "", category3.getId());

        // then
        assertThat(studyList1.getContent().size()).isEqualTo(2);
        assertThat(studyList1.getContent().get(0).getTitle()).isEqualTo("testTitle1");
        assertThat(studyList1.getContent().get(1).getTitle()).isEqualTo("testTitle1");
        assertThat(studyList2.getContent().size()).isEqualTo(1);
        assertThat(studyList2.getContent().get(0).getTitle()).isEqualTo("testTitle2");
        assertThat(studyList3.getContent().size()).isEqualTo(1);
        assertThat(studyList3.getContent().get(0).getTitle()).isEqualTo("testTitle3");
    }

    // search(title)
    @Test
    public void searchTypeTitle() {
        // given
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle12", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto3 = new StudyCreateRequestDto("testTitle123", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));

        // when
        studyService.createStudy(dto1, member1.getId());
        studyService.createStudy(dto2, member1.getId());
        studyService.createStudy(dto3, member1.getId());
        Page<StudyListResponseDto> studyList1 = studyService.getStudyList(0, 9, "title", "testTitle1", category1.getId());
        Page<StudyListResponseDto> studyList2 = studyService.getStudyList(0, 9, "title", "testTitle12", category1.getId());
        Page<StudyListResponseDto> studyList3 = studyService.getStudyList(0, 9, "title", "testTitle123", category1.getId());

        // then
        assertThat(studyList1.getTotalElements()).isEqualTo(3);
        assertThat(studyList2.getTotalElements()).isEqualTo(2);
        assertThat(studyList3.getTotalElements()).isEqualTo(1);
    }
    // search(content)
    @Test
    public void searchTypeContent() {
        // given
        Category category1 = categoryRepository.save(new Category("testCategory1"));
        StudyCreateRequestDto dto1 = new StudyCreateRequestDto("testTitle1", "testContent1", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto2 = new StudyCreateRequestDto("testTitle1", "testContent12", category1.getId(), List.of("testTag1", "testTag2"));
        StudyCreateRequestDto dto3 = new StudyCreateRequestDto("testTitle1", "testContent123", category1.getId(), List.of("testTag1", "testTag2"));

        // when
        studyService.createStudy(dto1, member1.getId());
        studyService.createStudy(dto2, member1.getId());
        studyService.createStudy(dto3, member1.getId());
        Page<StudyListResponseDto> studyList1 = studyService.getStudyList(0, 9, "content", "testContent1", category1.getId());
        Page<StudyListResponseDto> studyList2 = studyService.getStudyList(0, 9, "content", "testContent12", category1.getId());
        Page<StudyListResponseDto> studyList3 = studyService.getStudyList(0, 9, "content", "testContent123", category1.getId());

        // then
        assertThat(studyList1.getTotalElements()).isEqualTo(3);
        assertThat(studyList2.getTotalElements()).isEqualTo(2);
        assertThat(studyList3.getTotalElements()).isEqualTo(1);
    }
    // search(createdBy)
    @Test
    public void searchTypeCreatedBy() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("user22", "password3", "email3", ProviderType.LOCAL);
        Category category = categoryRepository.save(new Category("testCategory1"));
        StudyCreateRequestDto dto = new StudyCreateRequestDto("testTitle1", "testContent1", category.getId(), List.of("testTag1", "testTag2"));

        // when
        member2 = memberService.registerMember(signupRequestDto);
        studyService.createStudy(dto, member2.getId());
        Page<StudyListResponseDto> studyList1 = studyService.getStudyList(0, 9, "createdBy", "user", null);
        Page<StudyListResponseDto> studyList2 = studyService.getStudyList(0, 9, "createdBy", "user1", null);
        Page<StudyListResponseDto> studyList3 = studyService.getStudyList(0, 9, "createdBy", "user2", null);
        Page<StudyListResponseDto> studyList4 = studyService.getStudyList(0, 9, "createdBy", "user22", null);

        // when
        assertThat(studyList1.getTotalElements()).isEqualTo(3);
        assertThat(studyList2.getTotalElements()).isEqualTo(1);
        assertThat(studyList3.getTotalElements()).isEqualTo(2);
        assertThat(studyList4.getTotalElements()).isEqualTo(1);
    }
    // search(tag + countQuery)
    @Test
    public void searchTypeTag() {
        // given
        Category category = categoryRepository.save(new Category("testCategory1"));
        StudyCreateRequestDto dto = new StudyCreateRequestDto("testTitle1", "testContent1", category.getId(), List.of("testTag1", "testTag2", "testTag5"));

        // when
        studyService.createStudy(dto, member1.getId());
        Page<StudyListResponseDto> studyList1 = studyService.getStudyList(0, 9, "tag", "", null);
        Page<StudyListResponseDto> studyList2 = studyService.getStudyList(0, 9, "tag", "testTag1", null);
        Page<StudyListResponseDto> studyList3 = studyService.getStudyList(0, 9, "tag", "testTag1, testTag2", null);
        Page<StudyListResponseDto> studyList4 = studyService.getStudyList(0, 9, "tag", "testTag1, testTag2, testTag5", null);
        Page<StudyListResponseDto> studyList5 = studyService.getStudyList(0, 9, "tag", "testTag3", null);

        // then
        assertThat(studyList1.getTotalElements()).isEqualTo(3);
        assertThat(studyList2.getTotalElements()).isEqualTo(2);
        assertThat(studyList3.getTotalElements()).isEqualTo(2);
        assertThat(studyList4.getTotalElements()).isEqualTo(1);
        assertThat(studyList5.getTotalElements()).isEqualTo(1);
    }
}
