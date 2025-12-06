package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyCreateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "카테고리를 입력해주세요.")
    private Long categoryId;
    private List<String> tagNames;

    public StudyCreateRequestDto(String title, String content, Long categoryId, List<String> tagNames) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.tagNames = tagNames;
    }
}
