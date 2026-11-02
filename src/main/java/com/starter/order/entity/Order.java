package com.starter.order.entity;

import com.starter.admin.entity.Admin;
import com.starter.common.entity.BaseEntity;
import com.starter.customer.entity.Customer;
import com.starter.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = true)
    private Admin registerAdmin;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = true, length = 255)
    private String cancelReason;

    // 생성자
    protected Order() {
    }

    public Order(Customer customer, Admin registerAdmin, String orderNumber, Long totalPrice) {
        this.customer = customer;
        this.registerAdmin = registerAdmin;
        this.orderNumber = orderNumber;
        this.status = OrderStatus.PREPARING;
        this.totalPrice = totalPrice;
    }

    // 기능

    /**
     * 주문 상태를 다음 단계로 바꾼다.
     * <ul>
     *     <li>상태 변경 순서: 준비중 → 배송중 → 배송완료</li>
     * </ul>
     */
    public void updateOrderStatus() {

        if (this.status == OrderStatus.PREPARING) {
            this.status = OrderStatus.SHIPPING;
        } else if (this.status == OrderStatus.SHIPPING) {
            this.status = OrderStatus.DELIVERED;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "배송완료 또는 취소된 주문은 상태를 수정할 수 없습니다.");
        }

    }

    /**
     * 주문 상태를 취소로 바꾼다.
     * <ul>
     *     <li>준비중(PREPARING) 상태만 취소 가능</li>
     *     <li>배송중(SHIPPING) / 배송완료(DELIVERED)는 취소 불가</li>
     * </ul>
     *
     * @param cancelReason 취소 사유
     */
    public void cancelOrder(String cancelReason) {

        if (this.status == OrderStatus.PREPARING) {
            this.status = OrderStatus.CANCELLED;
        } else if (this.status == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 취소된 주문입니다.");
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "배송중 또는 배송완료된 주문은 취소할 수 없습니다.");
        }

        this.cancelReason = cancelReason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Admin getRegisterAdmin() {
        return registerAdmin;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public String getCancelReason() {
        return cancelReason;
    }
}
