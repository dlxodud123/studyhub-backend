package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "Username은 필수입니다.")
    private String username;
    @NotBlank(message = "Password는 필수입니다.")
    private String password;
    @NotBlank(message = "Email은 필수입니다.")
    private String email;

    public SignupRequestDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
