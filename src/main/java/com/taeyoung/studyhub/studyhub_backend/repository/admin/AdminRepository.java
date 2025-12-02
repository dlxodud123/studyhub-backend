package com.taeyoung.studyhub.studyhub_backend.repository.admin;

import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;

import java.util.List;

public interface AdminRepository {
    List<AdminDashboardResponseDto.RecentMember> findRecentMembers();
    List<AdminDashboardResponseDto.RecentStudy> findRecentStudies();
}
