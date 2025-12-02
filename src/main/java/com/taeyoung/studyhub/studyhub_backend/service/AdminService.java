package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.admin.AdminRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final AdminRepository adminRepository;

    public AdminDashboardResponseDto getDashboardData() {

        long memberCount = memberRepository.count();
        long studyCount = studyRepository.count();

        List<AdminDashboardResponseDto.RecentMember> recentMembers = adminRepository.findRecentMembers();
        List<AdminDashboardResponseDto.RecentStudy> recentStudies = adminRepository.findRecentStudies();

        return new AdminDashboardResponseDto(
            memberCount,
            studyCount,
            recentMembers,
            recentStudies
        );
    }
}
