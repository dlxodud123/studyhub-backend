package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void createStudy(StudyCreateRequestDto studyCreateRequestDto, Long userId) {
        Member member = memberRepository.getReferenceById(userId);

        Study study = new Study(
            studyCreateRequestDto.getTitle(),
            studyCreateRequestDto.getContent(),
            member
        );

        studyRepository.save(study);
    }
}
