package com.starter.product.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.starter.product.entity.Product;

import java.time.LocalDateTime;

// 순서 고정
@JsonPropertyOrder({"id", "name", "category", "price", "stock",
        "status", "createdAt", "adminName", "adminEmail"})

public class ProductDetailResponse {

    // 속성
    private Long id;
    private String name;
    private String category;
    private String price;
    private Integer stock;
    private Product.ProductStatus status;
    private LocalDateTime createdAt;
    private String adminName;
    private String adminEmail;

    // 생성자
    public ProductDetailResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = String.format("%,d원", product.getPrice());
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
        if(product.getAdmin() != null) {
            this.adminName = product.getAdmin().getName();
            this.adminEmail = product.getAdmin().getEmail();
        }
    }
    // 기능
    // 응답에 필요한 getter
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public String getPrice() {
        return price;
    }
    public Integer getStock() {
        return stock;
    }
    public Product.ProductStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getAdminName() {
        return adminName;
    }
    public String getAdminEmail() {
        return adminEmail;
    }

}
