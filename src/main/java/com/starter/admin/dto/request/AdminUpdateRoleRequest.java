package com.starter.admin.dto.request;

import com.starter.admin.enums.AdminRole;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자 역할 변경 요청 DTO
 */
public class AdminUpdateRoleRequest {
    //속성
    @NotNull(message = "변경할 권한 선택은 필수입니다.")
    private AdminRole role;

    //생성자
    public AdminUpdateRoleRequest(AdminRole role) {
        this.role = role;
    }

    //기능
    public AdminRole getRole() {
        return role;
    }
}
