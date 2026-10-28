package com.starter.admins.repository;

import com.starter.admins.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);
}
