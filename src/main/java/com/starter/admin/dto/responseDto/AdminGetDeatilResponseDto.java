package com.starter.admin.dto.responseDto;

import com.starter.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminGetDeatilResponseDto {
    //속성
    private String name;
    private String email;
    private String phone;
    private Admin.AdminRole role;
    private Admin.AdminStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    // 생성자
    public AdminGetDeatilResponseDto(
            String name,
            String email,
            String phone,
            Admin.AdminRole role,
            Admin.AdminStatus status,
            LocalDateTime createdAt,
            LocalDateTime approvedAt
    ) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public AdminGetDeatilResponseDto(
            String name,
            String email,
            String phone,
            LocalDateTime createdAt,
            LocalDateTime approvedAt
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }


    //기능

}