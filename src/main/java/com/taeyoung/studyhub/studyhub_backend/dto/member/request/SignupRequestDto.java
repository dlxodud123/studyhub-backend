package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import lombok.Data;

@Data
public class SignupRequestDto {

    private String username;
    private String password;
    private String email;

    public SignupRequestDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
