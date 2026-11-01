package com.starter.admin.dto.response;

import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;

import java.time.LocalDateTime;

/**
 * 관리자 상세 조회 응답 DTO
 */
public class AdminGetInfoResponse {
    //속성
    private String name;
    private String email;
    private String phone;
    private AdminRole role;
    private AdminStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;

    // 생성자
    public AdminGetInfoResponse(
            String name,
            String email,
            String phone,
            AdminRole role,
            AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime activatedAt
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.activatedAt = activatedAt;
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

    public AdminRole getRole() {
        return role;
    }

    public AdminStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }
}