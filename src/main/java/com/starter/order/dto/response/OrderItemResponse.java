package com.starter.order.dto.response;


public class OrderItemResponse {

    // 속성
    private String productName;
    private Long productPrice;
    private Integer quantity;
    private Long subtotal;

    // 생성자
    public OrderItemResponse(
            String productName,
            Long productPrice,
            Integer quantity
    ) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = productPrice * quantity; // 소계 = 주문 당시 단가 * 수량
    }

    // 기능
    public String getProductName() {
        return productName;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getSubtotal() {
        return subtotal;
    }
}
