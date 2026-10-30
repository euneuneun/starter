package com.starter.product.dto;

import com.starter.product.entity.Product;

import java.time.LocalDateTime;

public class ProductStatusResponse {

    // 속성
    private Long id;
    private Product.ProductStatus status;
    private LocalDateTime updatedAt;

    // 생성자
    public ProductStatusResponse(Long id, Product.ProductStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // 기능
    public Long getId() {
        return id;
    }
    public Product.ProductStatus getStatus() {
        return status;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
