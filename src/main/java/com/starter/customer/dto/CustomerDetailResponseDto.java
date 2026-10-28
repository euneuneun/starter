package com.starter.customer.dto;

import com.starter.customer.entity.Customer;

import java.time.LocalDateTime;

public class CustomerDetailResponseDto {

    // 속성
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Customer.CustomerStatus status;
    private LocalDateTime createdAt;

    // 생성자
    public CustomerDetailResponseDto(
            Long id, String name, String email, String phone, Customer.CustomerStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 기능
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public Customer.CustomerStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
