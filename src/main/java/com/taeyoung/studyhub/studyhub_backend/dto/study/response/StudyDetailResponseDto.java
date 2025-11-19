package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

@Getter
public class StudyDetailResponseDto {

    private String title;
    private String createdBy;
    private String createdAt;
    private String content;

    public StudyDetailResponseDto(String title, String createdBy, String createdAt, String content) {
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.content = content;
    }
}
