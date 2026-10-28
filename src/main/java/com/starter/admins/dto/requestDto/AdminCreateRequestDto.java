package com.starter.admins.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminCreateRequestDto {

    //속성
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "010-?\\d{3,4}-?\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    @NotBlank(message = "권한은 필수 선택입니다.")
    private String role;

    //생성자

    public AdminCreateRequestDto(String name, String email, String password, String phone, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    //기능


}
