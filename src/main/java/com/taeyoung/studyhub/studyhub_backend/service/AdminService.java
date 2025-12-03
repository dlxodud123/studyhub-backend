package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminStudiesResponseDto;
import com.taeyoung.studyhub.studyhub_backend.repository.admin.AdminRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.member.MemberRepository;
import com.taeyoung.studyhub.studyhub_backend.repository.study.StudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // members
    public Page<AdminMembersResponseDto> getMembers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findMembers(pageable);
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        memberRepository.delete(member);
    }

    public void changeRole(Long memberId, String role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setRole(Role.valueOf(role));
    }


    // studies
    public Page<AdminStudiesResponseDto> getStudies(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findStudies(pageable);
    }
}
