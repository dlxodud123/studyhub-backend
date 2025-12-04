package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String username;
    private String password;
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
