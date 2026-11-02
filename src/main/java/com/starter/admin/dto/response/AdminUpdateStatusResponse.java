package com.starter.admin.dto.response;

import com.starter.admin.enums.AdminStatus;

import java.time.LocalDateTime;

/**
 * 관리자 상태 변경 응답 DTO
 */
public class AdminUpdateStatusResponse {
    // 속성
    private Long adminId;
    private String adminName;
    private AdminStatus adminStatus;
    private LocalDateTime updatedAt;

    //생성자
    public AdminUpdateStatusResponse(Long adminId, String adminName, AdminStatus adminStatus, LocalDateTime updateAt) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminStatus = adminStatus;
        this.updatedAt = updateAt;
    }

    //기능
    public Long getAdminId() {
        return adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
