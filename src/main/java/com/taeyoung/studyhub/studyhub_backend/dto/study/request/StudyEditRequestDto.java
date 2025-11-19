package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import lombok.Getter;

@Getter
public class StudyEditRequestDto {

    private String title;
    private String content;

    public StudyEditRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
