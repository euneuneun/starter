package com.starter.customer.repository;

import com.starter.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 조건 검색
    Page<Customer> findByNameContainingOrEmailContaining(
            String keyword1, String keyword2, Pageable pageable);

    Page<Customer> findByStatus(
            Customer.CustomerStatus status, Pageable pageable);

    Page<Customer> findByNameContainingAndStatusOrEmailContainingAndStatus(
            String keyword1, Customer.CustomerStatus status1,
            String keyword2, Customer.CustomerStatus status2, Pageable pageable);
}
