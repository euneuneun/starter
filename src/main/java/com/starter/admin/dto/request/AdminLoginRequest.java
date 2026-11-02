package com.starter.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 관리자 로그인 요청 DTO
 */
public class AdminLoginRequest {
    //속성
    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String adminEmail;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    //생성자
    public AdminLoginRequest(String adminEmail, String password) {
        this.adminEmail = adminEmail;
        this.password = password;
    }

    //기능
    public String getAdminEmail() {
        return adminEmail;
    }

    public String getPassword() {
        return password;
    }
}
