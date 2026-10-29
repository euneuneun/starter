package com.starter.admins.dto.responseDto;


import com.starter.admins.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AdminListResponseDto {
    //속성
    private List<AdminDto> adminList;

    //생성자
    public AdminListResponseDto(List<AdminDto> adminList) {
        this.adminList = adminList;
    }
    //기능


    // 내부 클래스
    @Getter
    public static class AdminDto {

        //속성
        private Long adminId;
        private String name;
        private String email;
        private String phone;
        private Admin.AdminRole role;
        private Admin.AdminStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime approvedAt;

        //생성자
        public AdminDto(
                Long adminId,
                String name,
                String email,
                String phone,
                Admin.AdminRole role,
                Admin.AdminStatus status,
                LocalDateTime createdAt,
                LocalDateTime approvedAt
        ) {
            this.adminId = adminId;
            this.email = name;
            this.name = email;
            this.phone = phone;
            this.role = role;
            this.status = status;
            this.createdAt = createdAt;
            this.approvedAt = approvedAt;
        }
        //기능


    }
}
