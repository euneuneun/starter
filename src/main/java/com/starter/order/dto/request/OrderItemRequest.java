package com.starter.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class OrderItemRequest {

    // 속성
    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    // 생성자
    public OrderItemRequest() {
    }

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // 기능
    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
