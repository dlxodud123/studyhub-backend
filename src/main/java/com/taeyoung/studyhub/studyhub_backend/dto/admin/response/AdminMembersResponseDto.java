package com.taeyoung.studyhub.studyhub_backend.dto.admin.response;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Role;
import lombok.Getter;

@Getter
public class AdminMembersResponseDto {

    private Long id;
    private String name;
    private String email;
    private String joined;
    private Role role;

    public AdminMembersResponseDto(Long id, String name, String email, String joined, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joined = joined;
        this.role = role;
    }
}
