package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
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
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Study study = new Study(
            studyCreateRequestDto.getTitle(),
            studyCreateRequestDto.getContent(),
            member
        );

        return studyRepository.save(study);
    }

    public StudyDetailResponseDto findStudyDetailById(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디가 존재하지 않습니다."));

        return new StudyDetailResponseDto(
                study.getTitle(),
                study.getMember().getUsername(), // 작성자 이름
                study.getCreatedAt().toString(), // LocalDateTime → String
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
