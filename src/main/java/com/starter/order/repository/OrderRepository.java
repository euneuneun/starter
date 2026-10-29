package com.starter.order.repository;

import com.starter.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // SELECT COUNT(*) FROM orders WHERE order_number LIKE '20260808-%`
    long countByOrderNumberStartingWith(String today);
}
