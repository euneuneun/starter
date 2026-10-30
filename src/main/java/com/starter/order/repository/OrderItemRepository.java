package com.starter.order.repository;

import com.starter.order.entity.OrderItem;
import com.starter.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // SELECT order_items.* FROM order_items
    // JOIN orders ON order_items.order_id = orders.order_id
    // JOIN customers ON orders.customer_id = customer.id
    // WHERE (orders.order_number LIKE '%keyword% AND orders.status = 'status')
    // OR (customers.customer_name LIKE '%keyword2%' AND order.status = 'status')
    // ex) page = 2, size = 10, sortBy = createdAt, direction = DESC
    // ORDER BY order.created_at DESC
    // LIMIT 10
    // OFFSET 10; // page * size = 1 * 10 // 앞의 10개 (1페이지)를 건너 뛴다. 11~20번째 데이터가 나온다.
    Page<OrderItem> findByOrder_OrderNumberContainingAndOrder_StatusOrOrder_Customer_NameContainingAndOrder_Status(
            String keyword, OrderStatus status, String keyword2, OrderStatus status2, Pageable pageable);

    // SELECT * FROM order_items
    // JOIN orders ON order_items.order_id = orders.order_id
    // JOIN customers ON order_items_order.customer_id = customers.customer_id
    // WHERE (orders.order_number LIKE '%keyword')
    // OR (customers.customer_name LIKE 'keyword1')
    Page<OrderItem> findByOrder_OrderNumberContainingOrOrder_Customer_NameContaining(
            String keyword, String keyword2, Pageable pageable);

    // SELECT * FROM order_items
    // JOIN orders ON orders.order_id = order_items.order_id
    // WHERE  orders.status = 'status'
    Page<OrderItem> findByOrder_Status(OrderStatus status, Pageable pageable);
}
