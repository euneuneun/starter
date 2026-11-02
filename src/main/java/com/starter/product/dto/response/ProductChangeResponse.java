
package com.starter.product.dto.response;

import java.time.LocalDateTime;

public class ProductChangeResponse {

    private Long productId;
    private String productName;
    private String productCategory;
    private String price;
    private LocalDateTime updatedAt;
    /**
     * 상품 정보 수정 응답 DTO를 생성한다.
     *
     * @param productId 상품 ID
     * @param productName 상품명
     * @param productCategory 상품 카테고리
     * @param price 상품 가격
     * @param updatedAt 최종 수정 일시
     */
    public ProductChangeResponse(
            Long productId,
            String productName,
            String productCategory,
            Long price,
            LocalDateTime updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.price = String.format("%,d원", price);
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
     * 상품명을 반환한다.
     *
     * @return 상품명
     */
    public String getProductName() {
        return productName;
    }
    /**
     * 상품 카테고리를 반환한다.
     *
     * @return 상품 카테고리
     */
    public String getProductCategory() {
        return productCategory;
    }
    /**
     * 상품 가격을 반환한다.
     *
     * @return 상품 가격
     */
    public String getPrice() {
        return price;
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