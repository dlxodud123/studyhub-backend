package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StudyCreateRequestDto {
    private String title;
    private String content;
    private Long categoryId;
    private List<String> tagNames;

    public StudyCreateRequestDto(String title, String content, Long categoryId, List<String> tagNames) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.tagNames = tagNames;
    }
}
