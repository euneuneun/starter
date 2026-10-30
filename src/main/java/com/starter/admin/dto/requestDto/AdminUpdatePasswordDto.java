package com.starter.admin.dto.requestDto;

import lombok.Getter;

@Getter
public class AdminUpdatePasswordDto {
    //속성
    private String oldPassword;
    private String newPassword;

    //생성자

    public AdminUpdatePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }


    //기능
}
