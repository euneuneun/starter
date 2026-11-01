package com.starter.admin.dto.request;

import com.starter.admin.enums.AdminStatus;
import jakarta.validation.constraints.NotNull;

/**
 * 관리자 상태 변경 요청 DTO
 */
public class AdminUpdateStatusRequest {
    //속성
    @NotNull(message = "변경할 상태 선택은 필수입니다.")
    private AdminStatus status;
    private String rejectReason;

    //생성자
    public AdminUpdateStatusRequest(AdminStatus status, String rejectReason) {
        this.status = status;
        this.rejectReason = rejectReason;
    }

    //기능
    public AdminStatus getStatus() {
        return status;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}

