package com.starter.admin.dto.response;

import com.starter.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateRoleResponseDto {
    // 속성
    private Long adminId;
    private String name;
    private Admin.AdminRole role;
    private LocalDateTime updateAt;

    // 생성자

    public AdminUpdateRoleResponseDto(Long adminId, String name, Admin.AdminRole role, LocalDateTime updateAt) {
        this.adminId = adminId;
        this.name = name;
        this.role = role;
        this.updateAt = updateAt;
    }
    // 기능
}
