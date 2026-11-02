
package com.starter.product.dto.response;

import java.util.List;

public class ProductListResponse {
    private final List<ProductDetailResponse> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    /**
     * 상품 목록 조회 응답 DTO를 생성한다.
     *
     * @param content 상품 상세 정보 목록
     * @param page 현재 페이지 번호
     * @param size 페이지당 상품 수
     * @param totalElements 전체 상품 수
     * @param totalPages 전체 페이지 수
     */
    public ProductListResponse(
            List<ProductDetailResponse> content,
            Integer page,
            Integer size,
            Long totalElements,
            Integer totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
    /**
     * 상품 상세 정보 목록을 반환한다.
     *
     * @return 상품 상세 정보 목록
     */
    public List<ProductDetailResponse> getContent() {
        return content;
    }
    /**
     * 현재 페이지 번호를 반환한다.
     *
     * @return 현재 페이지 번호
     */
    public Integer getPage() {
        return page;
    }
    /**
     * 페이지당 상품 수를 반환한다.
     *
     * @return 페이지당 상품 수
     */
    public Integer getSize() {
        return size;
    }
    /**
     * 전체 상품 수를 반환한다.
     *
     * @return 전체 상품 수
     */
    public Long getTotalElements() {
        return totalElements;
    }
    /**
     * 전체 페이지 수를 반환한다.
     *
     * @return 전체 페이지 수
     */
    public Integer getTotalPages() {
        return totalPages;
    }
}