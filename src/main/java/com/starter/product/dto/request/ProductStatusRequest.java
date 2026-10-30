package com.starter.product.dto.request;

public class ProductStatusRequest {

    // 속성
    private Boolean discontinued;

    // 생성자
    public ProductStatusRequest() {
    }
    public ProductStatusRequest(Boolean discontinued) {
        this.discontinued = discontinued;
    }

    // 기능
    public Boolean getDiscontinued() {
        return discontinued;
    }
}
