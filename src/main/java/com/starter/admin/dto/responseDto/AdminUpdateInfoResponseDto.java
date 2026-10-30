package com.starter.admin.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateInfoResponseDto {

    //속성
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    //생성자
    public AdminUpdateInfoResponseDto(
            String name,
            String email,
            String phone,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    //기능

}

