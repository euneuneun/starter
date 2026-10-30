package com.starter.order.controller;

import com.starter.order.dto.request.OrderCreateRequest;
import com.starter.order.dto.response.OrderDetailResponse;
import com.starter.order.dto.response.OrderSummaryResponse;
import com.starter.order.enums.OrderStatus;
import com.starter.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    /**
     * CS 주문 생성 API
     *
     * @param adminId 로그인하고 있는 사용자(= CS 관리자)
     * @param request 주문 고객 ID, 주문 상품 ID, 주문 상품 수량
     * @return body 없음
     */
    @PostMapping
    public ResponseEntity<Void> createOrder(
            @SessionAttribute(name = "ADMIN_ID", required = false) Long adminId,
            @Valid @RequestBody OrderCreateRequest request) {

        orderService.createOrder(
                adminId,
                request.getCustomerId(),
                request.getProductId(),
                request.getQaunitity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 주문 상세 조회 API
     *
     * @param orderId 조회할 주문 ID
     * @return
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @PathVariable Long orderId) {

        OrderDetailResponse response = orderService.getOrderDetail(orderId);

        return ResponseEntity.ok(response);
    }

    /**
     * 주문 목록 조회 API
     *
     * @param keyword 검색어 (주문번호, 고객명)
     * @param status 주문 상태 필터 (준비중, 배송중, 배송완료, 취소됨)/ null 값 = 전체 상태
     * @param sortBy 정렬 기준값 (주문번호, 고객명, 상품명, 수량, 금액, 주문일, 상태)
     * @param direction ASC, DESC
     * @param page 조회할 페이지 수
     * @param size 한 페이지에 들어가는 주문 수
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<OrderSummaryResponse>> getOrderList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction, // Spring이 제공하는 enum 값 (ASC, DESC)
            @RequestParam(defaultValue = "1") int page, // defaultValue의 모든 값은 String으로 받아 필요시 int 처리한다.
            @RequestParam(defaultValue = "10") int size) {

        Page<OrderSummaryResponse> response = orderService.getOrderList(keyword, status, sortBy, direction, page, size);

        return ResponseEntity.ok(response);
    }
}
