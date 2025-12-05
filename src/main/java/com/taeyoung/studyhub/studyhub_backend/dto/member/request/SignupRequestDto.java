package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    public SignupRequestDto(String username, String password, String email, ProviderType provider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
    }
}
