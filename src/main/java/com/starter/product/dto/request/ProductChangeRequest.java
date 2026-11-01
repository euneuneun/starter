
package com.starter.product.dto.request;

public class ProductChangeRequest {

    private String productName;
    private String productCategory;
    private Long price;
    /**
     * 기본 생성자.
     */
    public ProductChangeRequest() {
    }
    /**
     * 상품 정보 수정 요청 DTO를 생성한다.
     *
     * @param productName 상품명
     * @param productCategory 상품 카테고리
     * @param price 상품 가격
     */
    public ProductChangeRequest(
            String productName,
            String productCategory,
            Long price) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.price = price;
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
    public Long getPrice() {
        return price;
    }
}