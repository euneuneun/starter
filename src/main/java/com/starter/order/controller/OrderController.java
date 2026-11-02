package com.starter.order.controller;

import com.starter.order.dto.request.OrderCancelRequest;
import com.starter.order.dto.request.OrderCreateRequest;
import com.starter.order.dto.response.OrderDetailResponse;
import com.starter.order.dto.response.OrderSummaryResponse;
import com.starter.order.enums.OrderStatus;
import com.starter.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;


@RestController
@RequestMapping("/orders")
public class OrderController {

    // 속성
    private final OrderService orderService;

    // 생성자
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 기능

    /**
     * CS 주문 생성 API
     *
     * @param adminId 로그인하고 있는 사용자 ID(= CS 관리자)
     * @param request 주문 고객 ID, 주문 상품 ID, 주문 상품 수량
     * @return 201 CREATED, body 없음
     */
    @PostMapping
    public ResponseEntity<Void> createOrder(
            @SessionAttribute(name = "ADMIN_ID", required = false) Long adminId,
            @Valid @RequestBody OrderCreateRequest request) {

        // 1. 서비스 처리
        orderService.createOrder(adminId, request);

        // 2. Http
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

        // 3. 반환
        return response;
    }

    /**
     * 주문 상세 조회 API
     *
     * @param orderId 조회할 주문 ID
     * @return 200 OK, 주문 상세 정보 (주문번호, 고객 정보, 주문 상품, 상태 등)
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @PathVariable Long orderId) {

        // 1. 서비스 처리
        OrderDetailResponse orderDetailResponse = orderService.getOrderDetail(orderId);

        // 2. Http
        ResponseEntity<OrderDetailResponse> response = new ResponseEntity<>(orderDetailResponse, HttpStatus.OK);

        // 3. 반환
        return response;
    }

    /**
     * 주문 목록 조회 API
     *
     * @param keyword   검색어 (주문번호, 고객명)
     * @param status    주문 상태 필터 (준비중, 배송중, 배송완료, 취소됨)/ null 값 = 전체 상태
     * @param sortBy    정렬 기준값 (주문번호, 고객명, 상품명, 수량, 금액, 주문일, 상태)
     * @param direction ASC, DESC
     * @param page      조회할 페이지 번호 (1부터)
     * @return 200 OK, 주문 목록 (페이징 정보 포함)
     */
    @GetMapping
    public ResponseEntity<Page<OrderSummaryResponse>> getOrderList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction, // Spring이 제공하는 enum 값 (ASC, DESC)
            @RequestParam(defaultValue = "1") int page // defaultValue의 모든 값은 String으로 받아 필요시 int 처리한다.
    ) {

        // 1. 서비스 처리
        Page<OrderSummaryResponse> orderListResponse = orderService.getOrderList(
                keyword, status, sortBy, direction, page);

        // 2. Http
        ResponseEntity<Page<OrderSummaryResponse>> response = new ResponseEntity<>(orderListResponse, HttpStatus.OK);

        // 3. 반환
        return response;
    }

    /**
     * 주문 상태 수정 API
     *
     * @param orderId 수정할 주문 ID
     * @return 200 OK, body 없음
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId) {

        // 1. 서비스 처리
        orderService.updateOrderStatus(orderId);

        // 2. Http
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);

        // 3. 반환
        return response;
    }

    /**
     * 주문 취소 API
     *
     * @param orderId 취소할 주문 ID
     * @param reqeust 주문 취소 요청 (취소 사유)
     * @return 200 OK, body 없음
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest reqeust
    ) {

        // 1. 서비스 처리
        orderService.cancelOrder(orderId, reqeust);

        // 2. Http
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);

        // 3. 반환
        return response;
    }
}
