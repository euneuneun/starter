
package com.starter.product.dto.request;

import com.starter.product.entity.Product;

public class ProductCreateRequest {

    private String productName;
    private String productCategory;
    private Long price;
    private Integer stock;
    private Product.ProductStatus productStatus;

    /**
     * 기본 생성자.
     */
    public ProductCreateRequest() {
    }
    /**
     * 상품 등록 요청 DTO를 생성한다.
     *
     * @param productName 상품명
     * @param productCategory 상품 카테고리
     * @param price 상품 가격
     * @param stock 상품 재고
     * @param productStatus 상품 상태
     */
    public ProductCreateRequest(
            String productName,
            String productCategory,
            Long price,
            Integer stock,
            Product.ProductStatus productStatus) {

        this.productName = productName;
        this.productCategory = productCategory;
        this.price = price;
        this.stock = stock;
        this.productStatus = productStatus;
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
     * 상품명을 설정한다.
     *
     * @param productName 상품명
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    /**
     * 상품 카테고리를 설정한다.
     *
     * @param productCategory 상품 카테고리
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    /**
     * 상품 가격을 설정한다.
     *
     * @param price 상품 가격
     */
    public void setPrice(Long price) {
        this.price = price;
    }
    /**
     * 상품 재고를 설정한다.
     *
     * @param stock 상품 재고
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    /**
     * 상품 상태를 설정한다.
     *
     * @param productStatus 상품 상태
     */
    public void setProductStatus(Product.ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}