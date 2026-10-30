package com.starter.product.dto;

public class ProductChangeRequestDto {

    // 속성
    private String name;
    private String category;
    private Long price;

    // 생성자
    public ProductChangeRequestDto() {
    }
    public ProductChangeRequestDto(String name, String category, Long price) {
        this.name = name;
        this.category = category;
        this.price = price;
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
}
