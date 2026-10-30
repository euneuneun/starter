package com.starter.customer.dto.request;

public class CustomerChangeRequest {

    // 속성
    private String name;
    private String email;
    private String phone;

    // 생성자
    public CustomerChangeRequest() {
    }

    public CustomerChangeRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // 기능
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
}
