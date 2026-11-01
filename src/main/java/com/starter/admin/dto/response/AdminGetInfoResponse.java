package com.starter.admin.dto.response;

import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;

import java.time.LocalDateTime;

/**
 * 관리자 상세 조회 응답 DTO
 */
public class AdminGetInfoResponse {
    //속성
    private String adminName;
    private String adminEmail;
    private String adminPhone;
    private AdminRole role;
    private AdminStatus adminStatus;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;

    // 생성자
    public AdminGetInfoResponse(
            String adminName,
            String adminEmail,
            String adminPhone,
            AdminRole role,
            AdminStatus adminStatus,
            LocalDateTime createdAt,
            LocalDateTime activatedAt
    ) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPhone = adminPhone;
        this.role = role;
        this.adminStatus = adminStatus;
        this.createdAt = createdAt;
        this.activatedAt = activatedAt;
    }

    //기능
    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public AdminRole getRole() {
        return role;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }
}