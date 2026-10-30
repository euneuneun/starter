package com.starter.admin.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminLoginRequestDto {
    //속성

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    //생성자
    public AdminLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //기능
}
