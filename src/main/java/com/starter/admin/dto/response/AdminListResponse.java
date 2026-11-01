package com.starter.admin.dto.response;


import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 관리자 다건 조회 응답 DTO
 */
public class AdminListResponse {
    //속성
    private List<AdminResponse> adminList;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    //생성자
    public AdminListResponse(
            List<AdminResponse> adminList,
            int currentPage,
            int pageSize,
            long totalElements,
            int totalPages
    ) {
        this.adminList = adminList;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    //기능
    public List<AdminResponse> getAdminList() {
        return adminList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    // 내부 클래스(조회된 관리자 내용)
    public static class AdminResponse {
        //속성
        private Long adminId;
        private String adminName;
        private String adminEmail;
        private String adminPhone;
        private AdminRole role;
        private AdminStatus adminStatus;
        private LocalDateTime createdAt;
        private LocalDateTime activatedAt;

        //생성자
        public AdminResponse(
                Long adminId,
                String adminName,
                String adminEmail,
                String adminPhone,
                AdminRole role,
                AdminStatus adminStatus,
                LocalDateTime createdAt,
                LocalDateTime activatedAt
        ) {
            this.adminId = adminId;
            this.adminName = adminName;
            this.adminEmail = adminEmail;
            this.adminPhone = adminPhone;
            this.role = role;
            this.adminStatus = adminStatus;
            this.createdAt = createdAt;
            this.activatedAt = activatedAt;
        }

        //기능
        public Long getAdminId() {
            return adminId;
        }

        public String getAdminName() {
            return adminName;
        }

        public String getAdminEmail() {
            return adminEmail;
        }

        public String getAdminPhone() {
            return adminPhone;
        }

        public AdminRole getRole() {
            return role;
        }

        public AdminStatus getAdminStatus() {
            return adminStatus;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getActivatedAt() {
            return activatedAt;
        }
    }
}
