package com.starter.admins.repository;

import com.starter.admins.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
   // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 이메일로 관리자 조회
    Optional<Admin> findByEmail(String email);
}
