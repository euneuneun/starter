package com.starter.admin.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class AdminPageSetDto {
    // 속성 (기본값 설정)
    private int page = 1;
    private int limit = 10;
    private String sortBy = "createdAt";
    private String sortOrder = "desc";
    //생성자

    //기능
    public Pageable toPageable() {

        // 페이지 정렬 방향 설정
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 페이지 설정 적용
        Pageable result = PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));

        // 결과 반환
        return result;
    }
}
