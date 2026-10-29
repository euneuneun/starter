package com.starter.customer.dto;

import com.starter.customer.entity.Customer;

public class CustomerStatusRequestDto {

    // 속성
    private Customer.CustomerStatus status;

    // 생성자
    public CustomerStatusRequestDto() {
    }
    public CustomerStatusRequestDto(Customer.CustomerStatus status) {
        this.status = status;
    }

    // 기능
    public Customer.CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(Customer.CustomerStatus status) {
        this.status = status;
    }
}
