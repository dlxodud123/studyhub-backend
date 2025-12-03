package com.taeyoung.studyhub.studyhub_backend.repository.admin;

import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminStudiesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminRepository {
    List<AdminDashboardResponseDto.RecentMember> findRecentMembers();
    List<AdminDashboardResponseDto.RecentStudy> findRecentStudies();

    Page<AdminMembersResponseDto> findMembers(Pageable pageable);
    Page<AdminStudiesResponseDto> findStudies(Pageable pageable);
}
