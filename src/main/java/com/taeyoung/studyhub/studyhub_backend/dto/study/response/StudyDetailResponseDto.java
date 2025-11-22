package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

import java.util.List;

@Getter
public class StudyDetailResponseDto {

    private String title;
    private String createdBy;
    private String createdAt;
    private String content;
    private String categoryName;
    private List<String> tagNames;

    public StudyDetailResponseDto(String title, String createdBy, String createdAt, String content, String categoryName, List<String> tagNames) {
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.content = content;
        this.categoryName = categoryName;
        this.tagNames = tagNames;
    }
}
