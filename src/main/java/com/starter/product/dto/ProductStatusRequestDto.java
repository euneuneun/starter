package com.starter.product.dto;

public class ProductStatusRequestDto {

    // 속성
    private Boolean discontinued;

    // 생성자
    public ProductStatusRequestDto() {
    }
    public ProductStatusRequestDto(Boolean discontinued) {
        this.discontinued = discontinued;
    }

    // 기능
    public Boolean getDiscontinued() {
        return discontinued;
    }
}
