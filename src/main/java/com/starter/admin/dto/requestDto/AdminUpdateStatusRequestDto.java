package com.starter.admin.dto.requestDto;

import com.starter.admin.entity.Admin;
import lombok.Getter;

@Getter
public class AdminUpdateStatusRequestDto {
    //속성
    private Admin.AdminStatus status;
    private String rejectReason;

    //생성자
    public AdminUpdateStatusRequestDto(Admin.AdminStatus status, String rejectReason) {
        this.status = status;
        this.rejectReason = rejectReason;
    }

    //기능
}

