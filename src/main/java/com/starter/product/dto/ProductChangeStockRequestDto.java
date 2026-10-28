package com.starter.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductChangeStockRequestDto {

    // 속성
    @JsonProperty("stock") //Jackson 필드 접근 설정
    private Integer stock;

    // 생성자
    public ProductChangeStockRequestDto() {
    }

    public ProductChangeStockRequestDto(Integer stock) {
        this.stock = stock;
    }

    // 기능
    public Integer getStock() {
        return stock;
    }
}
