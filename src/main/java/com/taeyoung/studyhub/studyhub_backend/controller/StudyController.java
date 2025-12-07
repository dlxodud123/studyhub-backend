package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.CommentCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyEditRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.CommentListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyDetailResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyEditResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.response.StudyListResponseDto;
import com.taeyoung.studyhub.studyhub_backend.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    public Page<StudyListResponseDto> getStudyList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId
    ) {
        return studyService.getStudyList(page, size, searchType, keyword, categoryId);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStudy(@RequestBody StudyCreateRequestDto studyCreateRequestDto, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();

        studyService.createStudy(studyCreateRequestDto, user.getId());
        return ResponseEntity.ok("스터디 작성 완료!");
    }

    @GetMapping("/detail/{id}")
    public StudyDetailResponseDto getStudyDetail(@PathVariable Long id) {
        return studyService.findStudyDetailById(id);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<StudyEditResponseDto> getStudyEdit(@PathVariable Long id, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();

        return ResponseEntity.ok(studyService.findStudyEditById(id, user.getId()));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editStudy(@PathVariable Long id, @RequestBody StudyEditRequestDto studyEditRequestDto, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();

        studyService.editStudyById(studyEditRequestDto, id, user.getId());
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudy(@PathVariable Long id, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        studyService.deleteStudyById(id, user.getId());
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("/comments/{id}")
    public List<CommentListResponseDto> getCommentList(@PathVariable Long id){
        return studyService.getCommentList(id);
    }

    @PostMapping("/create/comments/{id}")
    public ResponseEntity<String> createComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto, @PathVariable Long id, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        studyService.createComment(commentCreateRequestDto.getContent(), user.getId(), id);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }
}