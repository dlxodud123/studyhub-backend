package com.taeyoung.studyhub.studyhub_backend.dto.member.request.find;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UsernameAndPasswordRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
