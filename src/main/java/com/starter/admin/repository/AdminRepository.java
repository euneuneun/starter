package com.starter.admin.repository;

import com.starter.admin.entity.Admin;
import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 관리자 엔티티 리포지토리 인터페이스
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    /**
     * 이메일 중복 확인
     * @param adminEmail 확인할 관리자 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String adminEmail);

    /**
     * 이메일로 관리자 조회
     * @param adminEmail 조회할 이메일
     * @return 관리자 Optional 객체
     */
    Optional<Admin> findByEmail(String adminEmail);

    /**
     * 관리자 역할로 조회
     * @param adminRole 관리자 역할
     * @return 관리자 Optional 객체
     */
    Optional<Admin> findByRole(AdminRole adminRole);

    /**
     * 관리자 다건 조회 필터링 조건 쿼리
     */
    @Query("SELECT a FROM Admin a WHERE " +
            // 검색 키워드 조건
            "(:keyword IS NULL OR :keyword = '' OR a.adminName LIKE %:keyword% OR a.adminEmail LIKE %:keyword%) AND " +
            // 역할 및 상태 필터 조건
            "(:role IS NULL OR a.role = :role) AND " +
            "(:adminStatus IS NULL OR a.adminStatus = :adminStatus)")
    Page<Admin> findByConditions(
            @Param("keyword") String keyword,
            @Param("role") AdminRole role,
            @Param("adminStatus") AdminStatus adminStatus,
            Pageable pageable
    );

}
