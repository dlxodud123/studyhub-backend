package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.dto.admin.request.AdminRoleChangeRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminStudiesResponseDto;
import com.taeyoung.studyhub.studyhub_backend.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<AdminDashboardResponseDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardData());
    }

    // members
    @GetMapping("/members")
    public Page<AdminMembersResponseDto> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminService.getMembers(page, size);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId){
        try {
            adminService.deleteMember(memberId);
            return ResponseEntity.ok("삭제 완료!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생");
        }
    }

    @PatchMapping("/members/{memberId}/role")
    public ResponseEntity<String> changeMemberRole(
            @PathVariable Long memberId,
            @RequestBody AdminRoleChangeRequestDto requestDto) {

        try {
            adminService.changeRole(memberId, requestDto.getRole());
            return ResponseEntity.ok("권한 변경 완료");
        } catch (IllegalArgumentException e) {
            // 회원이 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 그 외 예상치 못한 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("권한 변경 중 오류 발생");
        }
    }


    // studies
    @GetMapping("/studies")
    public Page<AdminStudiesResponseDto> getStudies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminService.getStudies(page, size);
    }

    @DeleteMapping("/studies/{studyId}")
    public ResponseEntity<String> deleteStudy(@PathVariable Long studyId){
        try {
            adminService.deleteStudy(studyId);
            return ResponseEntity.ok("삭제 완료!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("스터디가 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생");
        }
    }
}
