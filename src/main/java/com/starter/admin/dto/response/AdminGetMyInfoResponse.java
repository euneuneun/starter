package com.starter.admin.dto.response;


import java.time.LocalDateTime;

/**
 * 내 정보 조회 응답 DTO
 */
public class AdminGetMyInfoResponse {
    //속성
    private String adminName;
    private String adminEmail;
    private String adminPhone;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;

    // 생성자
    public AdminGetMyInfoResponse(
            String adminName,
            String adminEmail,
            String adminPhone,
            LocalDateTime createdAt,
            LocalDateTime activatedAt
    ) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPhone = adminPhone;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }
}

