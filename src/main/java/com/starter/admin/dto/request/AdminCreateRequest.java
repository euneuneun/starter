package com.starter.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starter.admin.enums.AdminRole;
import jakarta.validation.constraints.*;

/**
 * 관리자 회원가입 요청 DTO
 */
public class AdminCreateRequest {
    //속성
    @NotBlank
    private String adminName;

    @NotBlank
    @Email
    private String adminEmail;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Pattern(regexp = "010-\\d{4}-\\d{4}$")
    private String adminPhone;

    @NotNull
    private AdminRole role;

    //생성자
    public AdminCreateRequest(
            String adminName,
            String adminEmail,
            String password,
            String adminPhone,
            AdminRole role
    ) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.password = password;
        this.adminPhone = adminPhone;
        this.role = role;
    }

    //기능
    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public AdminRole getRole() {
        return role;
    }
}
