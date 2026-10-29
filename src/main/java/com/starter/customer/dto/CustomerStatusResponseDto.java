package com.starter.customer.dto;

import com.starter.customer.entity.Customer;

import java.time.LocalDateTime;

public class CustomerStatusResponseDto {

    // 속성
    private Long id;
    private Customer.CustomerStatus status;
    private LocalDateTime updatedAt;

    // 생성자
    public CustomerStatusResponseDto(Long id, Customer.CustomerStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // 기능
    public Long getId() {
        return id;
    }
    public Customer.CustomerStatus getStatus() {
        return status;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
