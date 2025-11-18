package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

@Getter
public class StudyListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
//    private int commentCount;
//    private int likeCount;


    public StudyListResponseDto(Long id, String title, String content, String createdBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
    }
}
