package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/members")
    public Page<AdminMembersResponseDto> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminService.getMembers(page, size);
    }
}
