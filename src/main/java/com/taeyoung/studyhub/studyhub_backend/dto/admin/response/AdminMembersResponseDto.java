package com.taeyoung.studyhub.studyhub_backend.dto.admin.response;

import lombok.Getter;

@Getter
public class AdminMembersResponseDto {

    private Long id;
    private String name;
    private String email;
    private String joined;

    public AdminMembersResponseDto(Long id, String name, String email, String joined) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joined = joined;
    }
}
