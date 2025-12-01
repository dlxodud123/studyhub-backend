package com.taeyoung.studyhub.studyhub_backend.dto.admin.response;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminDashboardResponseDto {
    private final Long userCount;
    private final Long studyCount;
    private final List<RecentUser> recentUsers;
    private final List<RecentStudy> recentStudies;

    public AdminDashboardResponseDto(Long userCount, Long studyCount, List<RecentUser> recentUsers, List<RecentStudy> recentStudies) {
        this.userCount = userCount;
        this.studyCount = studyCount;
        this.recentUsers = recentUsers;
        this.recentStudies = recentStudies;
    }

    // 최근 회원 DTO
    @Getter
    public static class RecentUser {
        private final Long id;
        private final String name;
        private final String email;
        private final String joined;

        public RecentUser(Long id, String name, String email, String joined) {
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
