
package com.starter.customer.repository;

import com.starter.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * 조건에 따라 삭제되지 않은 고객 목록을 조회한다.
     *
     * @param keyword 고객 이름 또는 이메일 검색어
     * @param status 고객 상태
     * @param pageable 페이징 및 정렬 정보
     * @return 검색 조건에 해당하는 고객 목록
     */
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL "
            + "AND (:keyword IS NULL "
            + "OR c.name LIKE CONCAT('%', :keyword, '%') "
            + "OR c.email LIKE CONCAT('%', :keyword, '%')) "
            + "AND (:status IS NULL OR c.status = :status)")
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("status") Customer.CustomerStatus status,
            Pageable pageable);
}