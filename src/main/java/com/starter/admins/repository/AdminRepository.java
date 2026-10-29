package com.starter.admins.repository;

import com.starter.admins.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 이메일로 관리자 조회
    Optional<Admin> findByEmail(String email);

    // 관리자 다건 조회 필터링 조건

    @Query("SELECT a FROM Admin a WHERE " +
            // 검색 키워드 조건
            "(:keyword IS NULL OR :keyword = '' OR a.name LIKE %:keyword% OR a.email LIKE %:keyword%) AND " +
            // 역할 및 상태 필터 조건
            "(:role IS NULL OR a.role = :role) AND " +
            "(:status IS NULL OR a.status = :status)")
    Page<Admin> findByConditions(
            @Param("keyword") String keyword,
            @Param("role") Admin.AdminRole role,
            @Param("status") Admin.AdminStatus status,
            Pageable pageable
    );

}
