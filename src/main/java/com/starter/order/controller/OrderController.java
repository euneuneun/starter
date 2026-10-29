package com.starter.order.controller;

import com.starter.order.dto.request.OrderCreateRequest;
import com.starter.order.dto.response.OrderDetailResponse;
import com.starter.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    /**
     *  CS 주문 생성 API
     * @param adminId 로그인하고 있는 사용자(= CS 관리자)
     * @param request 주문 고객 ID, 주문 상품 ID, 주문 상품 수량
     * @return body 없음
     */
    @PostMapping
    public ResponseEntity<Void> createOrder(
            @SessionAttribute(name = "ADMIN_ID", required = false) Long adminId,
            @Valid @RequestBody OrderCreateRequest request)
    {
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
    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @SessionAttribute(name = "ADMIN_ID", required = false) Long adminId,
            @PathVariable Long orderId){

        OrderDetailResponse response = orderService.getOrderDetail(orderId);

        return ResponseEntity.ok(response);
    }
}
