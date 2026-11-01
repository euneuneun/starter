package com.starter.admin.dto.response;


import java.time.LocalDateTime;

/**
 * 내 정보 조회 응답 DTO
 */
public class AdminGetMyInfoResponse {
    //속성
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;

    // 생성자
    public AdminGetMyInfoResponse(
            String name,
            String email,
            String phone,
            LocalDateTime createdAt,
            LocalDateTime activatedAt
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.activatedAt = activatedAt;
    }

    //기능
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }
}

