package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/api/studies")
    public List<StudyListResponseDto> getStudyList() {
        return studyService.getStudyList();
    }

    @PostMapping("/api/studies/create")
    public ResponseEntity<String> createStudy(@RequestBody StudyCreateRequestDto studyCreateRequestDto, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            studyService.createStudy(studyCreateRequestDto, userId);

            return ResponseEntity.ok("스터디 작성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("스터디 작성 중 오류가 발생했습니다.");
        }
    }
}
