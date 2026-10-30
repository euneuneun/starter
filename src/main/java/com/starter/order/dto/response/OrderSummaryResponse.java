package com.starter.order.dto.response;

import com.starter.order.enums.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderSummaryResponse {

    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String productName;
    private Integer quantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String adminName;

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
            ){
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
}