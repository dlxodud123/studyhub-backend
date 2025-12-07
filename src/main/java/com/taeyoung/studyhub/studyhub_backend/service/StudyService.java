package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.study.*;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.CommentListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.global.exception.IdNotMatchException;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CommentRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public Page<StudyListResponseDto> getStudyList(int page, int size, String searchType, String keyword, Long categoryId) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Study> studyPage;

        if ("tag".equals(searchType)) {
            List<String> tags = Optional.ofNullable(keyword)
                    .filter(s -> !s.isBlank())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .filter(t -> !t.isEmpty())
                            .toList())
                    .orElse(Collections.emptyList());
            studyPage = studyRepository.searchStudiesByTags(tags, categoryId, pageRequest);
        } else {
            studyPage = studyRepository.searchStudies(searchType, keyword, categoryId, pageRequest);
        }

        return studyPage
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
            ));
    }

    public Study createStudy(StudyCreateRequestDto studyCreateRequestDto, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Category category = categoryRepository.findById(studyCreateRequestDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

        Study study = new Study(studyCreateRequestDto.getTitle(), studyCreateRequestDto.getContent(), member, category);
        Study saveStudy = studyRepository.save(study);

        // 중복 태그 제거 + 순서 유지(연관관계 설정)
        Set<String> uniqueTagNames = new LinkedHashSet<>(studyCreateRequestDto.getTagNames());
        for (String tagName : uniqueTagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            StudyTag studyTag = new StudyTag();

            study.addStudyTag(studyTag);
            tag.addStudyTag(studyTag);
        }

        return saveStudy;
    }

    public StudyDetailResponseDto findStudyDetailById(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));

        return new StudyDetailResponseDto(
            study.getTitle(),
            study.getMember().getUsername(),
            study.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
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
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));

        if (!study.getMember().getId().equals(userId)) {
            throw new IdNotMatchException("작성자만 수정할 수 있습니다.");
        }

        return new StudyEditResponseDto(
            study.getTitle(),
            study.getMember().getUsername(),
            study.getContent()
        );
    }

    public void editStudyById(StudyEditRequestDto studyEditRequestDto, Long id, Long userId) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));

        if (!study.getMember().getId().equals(userId)) {
            throw new IdNotMatchException("작성자만 수정할 수 있습니다.");
        }

        study.editStudy(studyEditRequestDto.getTitle(), studyEditRequestDto.getContent());
    }

    public void deleteStudyById(Long id, Long userId) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));

        if (!study.getMember().getId().equals(userId)) {
            throw new IdNotMatchException("작성자만 삭제할 수 있습니다.");
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
                comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ))
            .toList();
    }

    public Comment createComment(String content, Long userId, Long studId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Study study = studyRepository.findById(studId)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));

        Comment comment = new Comment(content, member, study);
        return commentRepository.save(comment);
    }
}
