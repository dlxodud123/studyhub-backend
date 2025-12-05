package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateRequestDto {

    @NotBlank(message = "Password는 필수입니다.")
    private String password;
    @NotBlank(message = "Email은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    public UpdateRequestDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
