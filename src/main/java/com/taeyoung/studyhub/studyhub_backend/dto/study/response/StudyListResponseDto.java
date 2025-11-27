package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

import java.util.List;

@Getter
public class StudyListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private String categoryName;
    private List<String> tagNames;
    private int commentCount;

    public StudyListResponseDto(Long id, String title, String content, String createdBy, String categoryName, List<String> tagNames, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.categoryName = categoryName;
        this.tagNames = tagNames;
        this.commentCount = commentCount;
    }
}
