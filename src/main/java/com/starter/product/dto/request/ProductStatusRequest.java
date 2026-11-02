
package com.starter.product.dto.request;

public class ProductStatusRequest {

    private Boolean discontinued;
    /**
     * 기본 생성자.
     */
    public ProductStatusRequest() {
    }
    /**
     * 상품 상태 변경 요청 DTO를 생성한다.
     *
     * @param discontinued 상품 단종 여부
     */
    public ProductStatusRequest(Boolean discontinued) {
        this.discontinued = discontinued;
    }
    /**
     * 상품 단종 여부를 반환한다.
     *
     * @return 상품 단종 여부
     */
    public Boolean getDiscontinued() {
        return discontinued;
    }
}