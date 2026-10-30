package com.starter.product.dto.response;

import java.time.LocalDateTime;

public class ProductChangeResponse {

    // 속성
    private Long id;
    private String name;
    private String category;
    private String price;
    private LocalDateTime updatedAt;

    // 생성자
    public ProductChangeResponse(Long id, String name, String category,
                                 Long price, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = String.format("%,d원", price);
        this.updatedAt = updatedAt;
    }

    // 기능
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
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
