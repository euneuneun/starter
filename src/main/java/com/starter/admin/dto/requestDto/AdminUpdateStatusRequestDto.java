package com.starter.admin.dto.requestDto;

import com.starter.admin.entity.Admin;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class AdminUpdateStatusRequestDto {
    //속성
    private Admin.AdminStatus status;

    //생성자
    public AdminUpdateStatusRequestDto(Admin.AdminStatus status) {
        this.status = status;
    }

    //기능
}

