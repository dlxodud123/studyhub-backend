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
    public Page<StudyListResponseDto> getStudyList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<StudyListResponseDto> studyList = studyService.getStudyList(page, size);
        // 페이지 정보 출력
        System.out.println("현재 페이지: " + studyList.getNumber());
        System.out.println("페이지 크기(size): " + studyList.getSize());
        System.out.println("전체 페이지 수: " + studyList.getTotalPages());
        System.out.println("전체 데이터 수: " + studyList.getTotalElements());
        System.out.println("현재 페이지 데이터 수: " + studyList.getNumberOfElements());
        System.out.println("--------------------------------------");
        // 실제 content 출력
        studyList.getContent().forEach(item -> {
            System.out.println("ID: " + item.getId());
            System.out.println("제목: " + item.getTitle());
            System.out.println("작성자: " + item.getCreatedBy());
            System.out.println("카테고리: " + item.getCategoryName());
            System.out.println("댓글수: " + item.getCommentCount());
            System.out.println("태그: " + item.getTagNames());
            System.out.println("--------------------------------------");
        });
        return studyList;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStudy(@RequestBody StudyCreateRequestDto studyCreateRequestDto, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            studyService.createStudy(studyCreateRequestDto, userId);

            return ResponseEntity.ok("스터디 작성 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("스터디 작성 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/detail/{id}")
    public StudyDetailResponseDto getStudyDetail(@PathVariable Long id) {
        return studyService.findStudyDetailById(id);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getStudyEdit(@PathVariable Long id, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();

        try {
            return ResponseEntity.ok(studyService.findStudyEditById(id, user.getId()));
        } catch (IllegalArgumentException e) {
            // 상태 코드 403으로 권한/접근 제한 메시지 전송
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editStudy(@PathVariable Long id, @RequestBody StudyEditRequestDto studyEditRequestDto) {
        studyService.editStudyById(studyEditRequestDto, id);

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
        Long userId = user.getId();

        try {
            studyService.createComment(commentCreateRequestDto.getContent(), userId, id);
            return ResponseEntity.ok("댓글이 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            // Member나 Study가 없는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
}
