package com.starter.order.dto.response;

import com.starter.admin.enums.AdminRole;
import com.starter.order.enums.OrderStatus;

import java.time.LocalDateTime;


public class OrderDetailResponse {

    // 속성
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String productName;
    private Integer quantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminEmail;
    private AdminRole adminRole;

    // 생성자
    public OrderDetailResponse(
            String orderNumber,
            String customerName,
            String customerEmail,
            String productName,
            Integer quantity,
            Long totalPrice,
            LocalDateTime createdAt,
            OrderStatus orderStatus,
            String adminEmail,
            AdminRole adminRole
    ) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = orderStatus;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;
    }

    // 기능
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public AdminRole getAdminRole() {
        return adminRole;
    }
}
