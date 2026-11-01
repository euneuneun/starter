package com.starter.admin.dto.response;

import com.starter.admin.enums.AdminRole;

import java.time.LocalDateTime;

/**
 * 관리자 역할 변경 응답 DTO
 */
public class AdminUpdateRoleResponse {
    // 속성
    private Long adminId;
    private String name;
    private AdminRole role;
    private LocalDateTime updatedAt;

    // 생성자
    public AdminUpdateRoleResponse(
            Long adminId,
            String name,
            AdminRole role,
            LocalDateTime updateAt
    ) {
        this.adminId = adminId;
        this.name = name;
        this.role = role;
        this.updatedAt = updateAt;
    }

    // 기능
    public Long getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public AdminRole getRole() {
        return role;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
