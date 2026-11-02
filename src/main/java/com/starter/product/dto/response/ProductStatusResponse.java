
package com.starter.product.dto.response;

import com.starter.product.enums.ProductStatus;

import java.time.LocalDateTime;

public class ProductStatusResponse {

    private Long productId;
    private ProductStatus productStatus;
    private LocalDateTime updatedAt;
    /**
     * 상품 상태 변경 응답 DTO를 생성한다.
     *
     * @param productId 상품 ID
     * @param productStatus 상품 상태
     * @param updatedAt 최종 수정 일시
     */
    public ProductStatusResponse(
            Long productId,
            ProductStatus productStatus,
            LocalDateTime updatedAt) {
        this.productId = productId;
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
     * 상품 상태를 반환한다.
     *
     * @return 상품 상태
     */
    public ProductStatus getProductStatus() {
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