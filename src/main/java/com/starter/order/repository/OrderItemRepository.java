package com.starter.order.repository;

import com.starter.order.entity.OrderItem;
import com.starter.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 주문 목록 조회 (검색어, 상태 필터 + 페이징)
    // - keyword가 null이면 검색어 조건 생략, status가 null이면 상태 조건 생략
    // - JOIN FETCH로 주문, 고객, 관리자를 한 번에 가져온다. (N+1 방지)
    // - 관리자는 없을 수 있어서 LEFT JOIN
    // - countQuery: 페이징용 전체 개수 조회 (개수만 세므로 FETCH 없음)
    @Query(value = """
            SELECT oi FROM OrderItem oi
            JOIN FETCH oi.order o
            JOIN FETCH o.customer c
            LEFT JOIN FETCH o.registerAdmin a
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR c.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            """,
            countQuery = """
            SELECT COUNT(oi) FROM OrderItem oi
            JOIN oi.order o
            JOIN o.customer c
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR c.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            """)
    Page<OrderItem> searchOrders(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable);
}
