
package com.starter.customer.dto.response;

import java.util.List;

public class CustomerListResponse {

    private final List<CustomerDetailResponse> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    /**
     * 고객 목록 조회 응답 객체를 생성한다.
     *
     * @param content 고객 상세 정보 목록
     * @param page 현재 페이지 번호
     * @param size 페이지당 고객 수
     * @param totalElements 전체 고객 수
     * @param totalPages 전체 페이지 수
     */
    public CustomerListResponse(
            List<CustomerDetailResponse> content,
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
     * 고객 상세 정보 목록을 반환한다.
     *
     * @return 고객 상세 정보 목록
     */
    public List<CustomerDetailResponse> getContent() {
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
     * 페이지당 고객 수를 반환한다.
     *
     * @return 페이지당 고객 수
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 전체 고객 수를 반환한다.
     *
     * @return 전체 고객 수
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