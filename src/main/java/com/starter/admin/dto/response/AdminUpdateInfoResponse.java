package com.starter.admin.dto.response;



import java.time.LocalDateTime;

/**
 * 관리자 정보 수정 응답 DTO
 */
public class AdminUpdateInfoResponse {
    //속성
    private String adminName;
    private String adminEmail;
    private String adminPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성자
    public AdminUpdateInfoResponse(
            String adminName,
            String adminEmail,
            String adminPhone,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPhone = adminPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

