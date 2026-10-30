package com.starter.admins.dto.requestDto;

import lombok.Getter;

@Getter
public class AdminUpdateRequestDto {

    //속성
    private String name;
    private String email;
    private String phone;

    //생성자
    public AdminUpdateRequestDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    //기능

}
