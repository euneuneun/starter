package com.starter.order.controller;

import com.starter.order.dto.request.OrderCreateRequest;
import com.starter.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @SessionAttribute(name = "LOGIN_ADMIN", required = false) Long adminId,
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
}
