package com.starter.admin.dto.request;

import com.starter.admin.enums.AdminRole;
import jakarta.validation.constraints.*;

/**
 * 관리자 회원가입 요청 DTO
 */
public class AdminCreateRequest {
    //속성
    @NotBlank(message = "이름은 필수입니다.")
    private String adminName;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String adminEmail;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "010-?\\d{3,4}-?\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String adminPhone;

    @NotNull(message = "권한은 필수 선택입니다.")
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
