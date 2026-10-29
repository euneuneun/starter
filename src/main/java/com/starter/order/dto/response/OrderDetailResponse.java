package com.starter.order.dto.response;

import com.starter.admins.entity.Admin;
import com.starter.order.enums.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDetailResponse {

    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String productName;
    private Integer quanitity;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminEmail;
    private Admin.AdminRole adminRole;

    public OrderDetailResponse(
            String orderNumber,
            String customerName,
            String customerEmail,
            String productName,
            Integer quanitity,
            Long totalPrice,
            LocalDateTime createdAt,
            OrderStatus orderStatus,
            String adminEmail,
            Admin.AdminRole adminRole
    ){
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.quanitity = quanitity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = orderStatus;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;
    }
}
