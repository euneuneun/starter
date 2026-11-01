package com.starter.order.dto.request;

import jakarta.validation.constraints.NotBlank;


public class OrderCancelRequest {

    // 속성
    @NotBlank
    private String cancelReason;

    // 생성자
    public OrderCancelRequest() {
    }

    public OrderCancelRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    // 기능
    public String getCancelReason() {
        return cancelReason;
    }
}
