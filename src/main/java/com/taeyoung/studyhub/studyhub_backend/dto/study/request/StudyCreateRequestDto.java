package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StudyCreateRequestDto {
    private String title;
    private String content;
//    private Long categoryId;
//    private List<Long> tagIds;


    public StudyCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
