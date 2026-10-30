package com.starter.admin.dto.response;


import com.starter.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AdminListResponseDto {
    //속성
    private List<AdminDto> adminList;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    //생성자
    public AdminListResponseDto(
            List<AdminDto> adminList,
            int currentPage,
            int pageSize,
            long totalElements,
            int totalPages
    ) {
        this.adminList = adminList;
        this.currentPage = currentPage;
        this.pageSize =pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
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
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.role = role;
            this.status = status;
            this.createdAt = createdAt;
            this.approvedAt = approvedAt;
        }
        //기능


    }
}
