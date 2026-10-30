package com.starter.product.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.starter.product.entity.Product;

public class ProductCreateRequest {

    // 속성
    private String name;
    private String category;
    private Long price;
    private Integer stock;
    private Product.ProductStatus status;

    // 생성자
    @JsonCreator
    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, String category, Long price,
                                Integer stock, Product.ProductStatus status) {
        this.name = name;
        this.category = category;
        this.price = price;
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
    public Long getPrice() {
        return price;
    }
    public Integer getStock() {
        return stock;
    }
    public Product.ProductStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public void setStatus(Product.ProductStatus status) {
        this.status = status;
    }
}
