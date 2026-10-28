package com.starter.Admins;

import com.starter.Admins.Entity.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admins, Long> {
}
