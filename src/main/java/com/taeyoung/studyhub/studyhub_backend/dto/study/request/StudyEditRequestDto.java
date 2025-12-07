package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StudyEditRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public StudyEditRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
