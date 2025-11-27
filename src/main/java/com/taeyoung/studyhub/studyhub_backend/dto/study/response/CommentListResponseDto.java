package com.taeyoung.studyhub.studyhub_backend.dto.study.response;

import lombok.Getter;

@Getter
public class CommentListResponseDto {

    private Long commentId;
    private String content;
    private String createdBy;
    private String createdAt;

    public CommentListResponseDto(Long commentId, String content, String createdBy, String createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}
