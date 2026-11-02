
package com.starter.product.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.starter.product.entity.Product;
import com.starter.product.enums.ProductStatus;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "productId",
        "productName",
        "productCategory",
        "price",
        "stock",
        "productStatus",
        "createdAt",
        "adminName",
        "adminEmail"
})
public class ProductDetailResponse {

    private Long productId;
    private String productName;
    private String productCategory;
    private String price;
    private Integer stock;
    private ProductStatus productStatus;
    private LocalDateTime createdAt;
    private String adminName;
    private String adminEmail;

    /**
     * 상품 엔티티를 상세 조회 응답 DTO로 변환한다.
     *
     * @param product 상품 엔티티
     */
    public ProductDetailResponse(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productCategory = product.getProductCategory();
        this.price = String.format("%,d원", product.getPrice());
        this.stock = product.getStock();
        this.productStatus = product.getProductStatus();
        this.createdAt = product.getCreatedAt();

        if (product.getAdmin() != null) {
            this.adminName = product.getAdmin().getAdminName();
            this.adminEmail = product.getAdmin().getAdminEmail();
        }
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
    public ProductStatus getProductStatus() {
        return productStatus;
    }

    /**
     * 상품 등록 일시를 반환한다.
     *
     * @return 상품 등록 일시
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 상품 등록 관리자 이름을 반환한다.
     *
     * @return 관리자 이름
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * 상품 등록 관리자 이메일을 반환한다.
     *
     * @return 관리자 이메일
     */
    public String getAdminEmail() {
        return adminEmail;
    }
}