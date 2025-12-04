package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String username;
    private String password;
    private String email;
}
