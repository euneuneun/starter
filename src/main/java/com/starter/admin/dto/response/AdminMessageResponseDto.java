package com.starter.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminMessageResponseDto {

    //속성
    private final String AdminMessageResponseDto;

    //생성자
    public AdminMessageResponseDto(String message) {
        this.AdminMessageResponseDto = message;
    }

    //기능

}
