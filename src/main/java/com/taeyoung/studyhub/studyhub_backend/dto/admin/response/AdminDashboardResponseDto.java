package com.taeyoung.studyhub.studyhub_backend.dto.admin.response;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminDashboardResponseDto {
    private final Long memberCount;
    private final Long studyCount;
    private final List<RecentMember> recentMembers;
    private final List<RecentStudy> recentStudies;

    public AdminDashboardResponseDto(Long memberCount, Long studyCount, List<RecentMember> recentMembers, List<RecentStudy> recentStudies) {
        this.memberCount = memberCount;
        this.studyCount = studyCount;
        this.recentMembers = recentMembers;
        this.recentStudies = recentStudies;
    }

    // 최근 회원 DTO
    @Getter
    public static class RecentMember {
        private final Long id;
        private final String name;
        private final String email;
        private final String joined;

        public RecentMember(Long id, String name, String email, String joined) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.joined = joined;
        }
    }

    // 최근 스터디 DTO
    @Getter
    public static class RecentStudy {
        private final Long id;
        private final String name;
        private final String category;
        private final String created;

        public RecentStudy(Long id, String name, String category, String created) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.created = created;
        }
    }
}
