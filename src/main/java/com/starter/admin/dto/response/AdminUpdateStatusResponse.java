package com.starter.admin.dto.response;

import com.starter.admin.enums.AdminStatus;

import java.time.LocalDateTime;

/**
 * 관리자 상태 변경 응답 DTO
 */
public class AdminUpdateStatusResponse {
    // 속성
    private Long adminId;
    private String name;
    private AdminStatus status;
    private LocalDateTime updatedAt;

    //생성자
    public AdminUpdateStatusResponse(Long adminId, String name, AdminStatus status, LocalDateTime updateAt) {
        this.adminId = adminId;
        this.name = name;
        this.status = status;
        this.updatedAt = updateAt;
    }

    //기능
    public Long getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public AdminStatus getStatus() {
        return status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
