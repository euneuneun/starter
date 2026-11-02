package com.starter.order.dto.response;

import com.starter.order.enums.OrderStatus;

import java.time.LocalDateTime;


public class OrderSummaryResponse {

    // 속성
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String productName;
    private Integer quantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminName;

    // 생성자
    public OrderSummaryResponse(
            Long orderId,
            String orderNumber,
            String customerName,
            String productName,
            Integer quantity,
            Long totalPrice,
            LocalDateTime createdAt,
            OrderStatus status,
            String adminName
    ) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.adminName = adminName;
    }

    // 기능
    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
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

    public String getAdminName() {
        return adminName;
    }
}
