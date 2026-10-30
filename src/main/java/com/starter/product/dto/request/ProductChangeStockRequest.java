package com.starter.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductChangeStockRequest {

    // 속성
    @JsonProperty("stock") //Jackson 필드 접근 설정
    private Integer stock;

    // 생성자
    public ProductChangeStockRequest() {
    }

    public ProductChangeStockRequest(Integer stock) {
        this.stock = stock;
    }

    // 기능
    public Integer getStock() {
        return stock;
    }
}
