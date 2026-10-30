package com.starter.admin.dto.requestDto;

import com.starter.admin.entity.Admin;
import lombok.Getter;

@Getter
public class AdminUpdateRoleRequestDto {

    //속성
    private Admin.AdminRole role;

    //생성자
    public AdminUpdateRoleRequestDto(Admin.AdminRole role) {
        this.role = role;
    }

    //기능
}
