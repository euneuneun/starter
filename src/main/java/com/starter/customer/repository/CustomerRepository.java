package com.starter.customer.repository;

import com.starter.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 조건 검색
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL " +
            "AND (:keyword IS NULL OR c.name LIKE CONCAT('%', :keyword, '%') OR c.email LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:status IS NULL OR c.status = :status)")
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("status") Customer.CustomerStatus status,
            Pageable pageable);
}
