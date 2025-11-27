package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.study.*;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.CommentListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CommentRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public List<StudyListResponseDto> getStudyList(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Study> studyPage = studyRepository.findAll(pageRequest);
        List<Study> studies = studyPage.getContent();

        return studies.stream()
            .map(study -> new StudyListResponseDto(
                    study.getId(),
                    study.getTitle(),
                    study.getContent(),
                    study.getMember().getUsername(),
                    study.getCategory() != null ? study.getCategory().getName() : null,
                    study.getStudyTags()
                            .stream()
                            .map(st -> st.getTag().getName())
                            .toList(),
                    study.getComments().size()
            ))
            .toList();

//        for (Study study : studies) {
//            List<String> tagNames = study.getStudyTags()
//                    .stream()
//                    .map(st -> st.getTag().getName())
//                    .toList();
//
//            System.out.println(tagNames.toString());
//        }
//
//        return studies.stream()
//            .map(study -> new StudyListResponseDto(
//                    study.getId(),
//                    study.getTitle(),
//                    study.getContent(),
//                    study.getMember().getUsername(),
//                    study.getCategory() != null ? study.getCategory().getName() : null,
//                    study.getStudyTags()
//                            .stream()
//                            .map(st -> st.getTag().getName())
//                            .toList(),
//                    study.getComments().size()
//            ))
//            .toList();
    }

    public Study createStudy(StudyCreateRequestDto studyCreateRequestDto, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Category category = categoryRepository.findById(studyCreateRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Study study = new Study(studyCreateRequestDto.getTitle(), studyCreateRequestDto.getContent(), member, category);
        Study saveStudy = studyRepository.save(study);

        // 중복 태그 제거 + 순서 유지
        Set<String> uniqueTagNames = new LinkedHashSet<>(studyCreateRequestDto.getTagNames());

        for (String tagName : uniqueTagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            StudyTag studyTag = new StudyTag();
//            StudyTag studyTag = new StudyTag(study, tag);

            study.addStudyTag(studyTag);
            tag.addStudyTag(studyTag);
        }

        return saveStudy;
    }

    public StudyDetailResponseDto findStudyDetailById(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        return new StudyDetailResponseDto(
            study.getTitle(),
            study.getMember().getUsername(),
            study.getCreatedAt().toString(),
            study.getContent(),
            study.getCategory() != null ? study.getCategory().getName() : null,
            study.getStudyTags()
                    .stream()
                    .map(st -> st.getTag().getName())
                    .toList()

        );
    }

    public StudyEditResponseDto findStudyEditById(Long id, Long userId) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        if (!study.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        return new StudyEditResponseDto(
            study.getTitle(),
            study.getMember().getUsername(),
            study.getContent()
        );
    }

    public void editStudyById(StudyEditRequestDto studyEditRequestDto, Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

        study.editStudy(studyEditRequestDto.getTitle(), studyEditRequestDto.getContent());
    }

    public void deleteStudyById(Long id, Long userId) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        if (!study.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        studyRepository.delete(study);
    }

    public List<CommentListResponseDto> getCommentList(Long id) {
        List<Comment> comments = commentRepository.findByStudyId(id);

        return comments.stream()
            .map(comment -> new CommentListResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getUsername(),
                comment.getCreatedAt().toString()
            ))
            .toList();
    }

    public Comment createComment(String content, Long userId, Long studId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Study study = studyRepository.findById(studId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        Comment comment = new Comment(content, member, study);
        return commentRepository.save(comment);
    }
}
