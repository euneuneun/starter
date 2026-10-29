package com.starter.product.dto;

import com.starter.product.entity.Product;

import java.time.LocalDateTime;

public class ProductChangeStockResponseDto {

    // 속성
    private Long id;
    private Integer stock;
    private Product.ProductStatus status;
    private LocalDateTime updatedAt;

    // 생성자
    public ProductChangeStockResponseDto(
            Long id, Integer stock, Product.ProductStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.stock = stock;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // 기능
    public Long getId() {
        return id;
    }
    public Integer getStock() {
        return stock;
    }
    public Product.ProductStatus getStatus() {
        return status;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
