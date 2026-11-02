
package com.starter.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductChangeStockRequest {
    @JsonProperty("stock")
    private Integer stock;
    /**
     * 기본 생성자.
     */
    public ProductChangeStockRequest() {
    }
    /**
     * 상품 재고 변경 요청 DTO를 생성한다.
     *
     * @param stock 변경할 상품 재고
     */
    public ProductChangeStockRequest(Integer stock) {
        this.stock = stock;
    }
    /**
     * 상품 재고를 반환한다.
     *
     * @return 상품 재고
     */
    public Integer getStock() {
        return stock;
    }
}