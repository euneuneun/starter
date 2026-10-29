package com.starter.customer.dto;

import java.util.List;

public class CustomerListResponseDto {

    // 속성
    private final List<CustomerDetailResponseDto> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    // 생성자
    public CustomerListResponseDto(List<CustomerDetailResponseDto> content, Integer page,
                                   Integer size, Long totalElements, Integer totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    // 기능
    public List<CustomerDetailResponseDto> getContent() {
        return content;
    }
    public Integer getPage() {
        return page;
    }
    public Integer getSize() {
        return size;
    }
    public Long getTotalElements() {
        return totalElements;
    }
    public Integer getTotalPages() {
        return totalPages;
    }
}
