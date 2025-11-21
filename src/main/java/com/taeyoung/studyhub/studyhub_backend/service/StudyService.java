package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.domain.study.StudyTag;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Tag;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.CategoryRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public List<StudyListResponseDto> getStudyList() {

        List<Study> studies = studyRepository.findAll();

        return studies.stream()
            .map(study -> new StudyListResponseDto(
                    study.getId(),
                    study.getTitle(),
                    study.getContent(),
                    study.getMember().getUsername()
//                    study.getComments().size(),
//                    study.getLikes().size()
            ))
            .toList();
    }

    public Study createStudy(StudyCreateRequestDto studyCreateRequestDto, Long userId) {
        System.out.println("id : " + userId);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        System.out.println("member check");

        Category category = categoryRepository.findById(studyCreateRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        System.out.println("category check");

        Study study = new Study(studyCreateRequestDto.getTitle(), studyCreateRequestDto.getContent(), member, category);
        Study saveStudy = studyRepository.save(study);

        System.out.println("create study");

        for (String tagName : studyCreateRequestDto.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
//                    .orElseGet(() -> {
//                        Tag newTag = new Tag(tagName);
//                        return tagRepository.save(newTag);
//                    });

            System.out.println("create tag");

            StudyTag studyTag = new StudyTag();

            System.out.println("create studyTag");

//            study.addStudyTag(studyTag);
//            tag.addStudyTag(studyTag);
            studyTag.setStudy(study);   // StudyTag가 Study를 참조하도록
            studyTag.setTag(tag);        // StudyTag가 Tag를 참조하도록
            study.getStudyTags().add(studyTag);  // Study의 studyTags 리스트에 추가
        }

        System.out.println("연관관계");

        return saveStudy;
    }

    public StudyDetailResponseDto findStudyDetailById(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        return new StudyDetailResponseDto(
            study.getTitle(),
            study.getMember().getUsername(),
            study.getCreatedAt().toString(),
            study.getContent()
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

        studyRepository.deleteById(id);
    }
}
