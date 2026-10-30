package com.starter.admin.dto.requestDto;

import lombok.Getter;

@Getter
public class AdminUpdateInfoRequestDto {

    //속성
    private String name;
    private String email;
    private String phone;

    //생성자
    public AdminUpdateInfoRequestDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    //기능

}
