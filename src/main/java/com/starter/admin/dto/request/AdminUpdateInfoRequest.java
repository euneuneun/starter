package com.starter.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 관리자 정보 수정 요청 DTO
 */
public class AdminUpdateInfoRequest {
    //속성
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "010-?\\d{3,4}-?\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    //생성자
    public AdminUpdateInfoRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    //기능
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
