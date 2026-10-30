package com.starter.admin.dto.response;

import com.starter.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateStatusResponseDto {
    // 속성
    private Long adminId;
    private String name;
    private Admin.AdminStatus status;
    private LocalDateTime updateAt;

    //생성자

    public AdminUpdateStatusResponseDto(Long adminId, String name, Admin.AdminStatus status, LocalDateTime updateAt) {
        this.adminId = adminId;
        this.name = name;
        this.status = status;
        this.updateAt = updateAt;
    }


    //기능

}
