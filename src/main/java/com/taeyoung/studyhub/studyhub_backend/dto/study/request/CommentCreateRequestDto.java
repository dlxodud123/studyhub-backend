package com.taeyoung.studyhub.studyhub_backend.dto.study.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
