package com.starter.product.dto;

import com.starter.product.entity.Product;

public class ProductCreateResponse {

    // 속성
    private String name;
    private String category;
    private String price;
    private Integer stock;
    private Product.ProductStatus status;

    // 생성자
    public ProductCreateResponse(String name, String category, Long price, Integer stock, Product.ProductStatus status) {
        this.name = name;
        this.category = category;
        this.price = String.format("%,d원", price);
        this.stock = stock;
        this.status = status;
    }

    // 기능
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
}
