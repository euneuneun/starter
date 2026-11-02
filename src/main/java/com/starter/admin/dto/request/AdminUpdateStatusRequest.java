package com.starter.admin.dto.request;

import com.starter.admin.enums.AdminStatus;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자 상태 변경 요청 DTO
 */
public class AdminUpdateStatusRequest {
    //속성
    @NotNull(message = "변경할 상태 선택은 필수입니다.")
    private AdminStatus adminStatus;
    private String rejectReason;

    //생성자
    public AdminUpdateStatusRequest(AdminStatus adminStatus, String rejectReason) {
        this.adminStatus = adminStatus;
        this.rejectReason = rejectReason;
    }

    //기능SSSS
    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}

