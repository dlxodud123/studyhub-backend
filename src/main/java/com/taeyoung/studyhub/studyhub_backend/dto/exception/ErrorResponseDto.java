package com.taeyoung.studyhub.studyhub_backend.dto.exception;

import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private String code;
    private String message;

    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
