package com.taeyoung.studyhub.studyhub_backend.dto.member.request;

import lombok.Data;

@Data
public class SignupRequestDto {

    private String username;
    private String password;
    private String email;
}
