package com.starter.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderCancelRequest {

    @NotBlank
    private String cancelReason;
}
