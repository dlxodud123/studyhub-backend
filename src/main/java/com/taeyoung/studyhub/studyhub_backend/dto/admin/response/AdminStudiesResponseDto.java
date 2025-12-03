package com.taeyoung.studyhub.studyhub_backend.dto.admin.response;

import lombok.Getter;

@Getter
public class AdminStudiesResponseDto {

    private Long id;
    private String name;
    private String category;
    private String created;

    public AdminStudiesResponseDto(Long id, String name, String category, String created) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.created = created;
    }
}
