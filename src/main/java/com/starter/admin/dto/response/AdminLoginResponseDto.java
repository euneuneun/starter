package com.starter.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminLoginResponseDto {
    //속성
    private final String message;

    //생성자
    public AdminLoginResponseDto(String message) {
        this.message = message;
    }

    //기능

}
