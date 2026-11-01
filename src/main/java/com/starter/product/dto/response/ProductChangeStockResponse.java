
package com.starter.product.dto.response;

import com.starter.product.entity.Product;

import java.time.LocalDateTime;

public class ProductChangeStockResponse {

    private Long productId;
    private Integer stock;
    private Product.ProductStatus productStatus;
    private LocalDateTime updatedAt;
    /**
     * 상품 재고 변경 응답 DTO를 생성한다.
     *
     * @param productId 상품 ID
     * @param stock 상품 재고
     * @param productStatus 상품 상태
     * @param updatedAt 최종 수정 일시
     */
    public ProductChangeStockResponse(
            Long productId,
            Integer stock,
            Product.ProductStatus productStatus,
            LocalDateTime updatedAt) {
        this.productId = productId;
        this.stock = stock;
        this.productStatus = productStatus;
        this.updatedAt = updatedAt;
    }
    /**
     * 상품 ID를 반환한다.
     *
     * @return 상품 ID
     */
    public Long getProductId() {
        return productId;
    }
    /**
     * 상품 재고를 반환한다.
     *
     * @return 상품 재고
     */
    public Integer getStock() {
        return stock;
    }
    /**
     * 상품 상태를 반환한다.
     *
     * @return 상품 상태
     */
    public Product.ProductStatus getProductStatus() {
        return productStatus;
    }
    /**
     * 최종 수정 일시를 반환한다.
     *
     * @return 최종 수정 일시
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}