package com.starter.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class OrderCreateRequest {

    // 속성
    @NotNull
    private Long customerId;

    @NotNull
    private Long productId;

    @Min(1)
    private Integer qaunitity;

    // 생성자
    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long customerId, Long productId, Integer qauntity) {
        this.customerId = customerId;
        this.productId = productId;
        this.qaunitity = qauntity;
    }

    // 기능
    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQaunitity() {
        return qaunitity;
    }
}
