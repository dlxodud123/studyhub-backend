package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
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

    public List<StudyListResponseDto> getStudyList() {

//        List<Study> studies = studyRepository.findAll();

//        return studies.stream()
//            .map(study -> new StudyListResponseDto(
//                    study.getId(),
//                    study.getTitle(),
//                    study.getContent(),
//                    study.getMember().getUsername()
////                    study.getComments().size(),
////                    study.getLikes().size()
//            ))
//            .toList();

        return List.of(
            new StudyListResponseDto(
                    1L,
                    "title",
                    "content",
                    "lee"
            ),
            new StudyListResponseDto(
                    2L,
                    "hello world",
                    "this is dummy content",
                    "tester"
            )
        );
    }
}
