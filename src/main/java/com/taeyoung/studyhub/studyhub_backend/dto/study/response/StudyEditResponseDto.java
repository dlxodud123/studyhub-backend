package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

@Getter
public class StudyEditResponseDto {

    private String title;
    private String createdBy;
    private String content;

    public StudyEditResponseDto(String title, String createdBy, String content) {
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
    }
}
