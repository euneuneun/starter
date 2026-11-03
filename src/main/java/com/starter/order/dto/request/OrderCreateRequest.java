package com.starter.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class OrderCreateRequest {

    // 속성
    @NotNull
    private Long customerId;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    // 생성자
    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long customerId, List<OrderItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }

    // 기능
    public Long getCustomerId() {
        return customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }
}
