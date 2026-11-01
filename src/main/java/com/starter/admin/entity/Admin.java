package com.starter.admin.entity;

import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;
import com.starter.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * 관리자 엔티티 클래스
 */
@Entity
@Table(name = "admins")
@SQLRestriction("deleted_at IS NULL")
public class Admin extends BaseEntity {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AdminRole role;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AdminStatus status;

    @Column(nullable = true, length = 200)
    private String rejectReason;

    @Column(nullable = true)
    private LocalDateTime activatedAt;

    @Column(nullable = true)
    private LocalDateTime rejectedAt;

    //생성자
    // JPA 기본 생성자
    protected Admin() {
    }

    public Admin(
            String name,
            String email,
            String password,
            String phone,
            AdminRole role
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = AdminStatus.PENDING;
    }

    //기능
    /**
     *  Getter
     */
    public Long getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public AdminRole getRole() {
        return role;
    }

    public AdminStatus getStatus() {
        return status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    /**
     * 관리자 정보 수정 기능
     */
    public Admin changeInfo(String newName, String newEmail, String newPhone) {
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        return this;
    }

    /**
     *  관리자 역할 수정 기능
     */
    public Admin changeRole(AdminRole newRole) {
        this.role = newRole;
        return this;
    }

    /**
     *  관리자 상태 수정 기능
     */
    public Admin changeStatus(AdminStatus newStatus, String rejectReason) {
        this.status = newStatus;

        // 가입 승인, 거부 시 시간 기록, 거부 사유 절차
        if (newStatus == AdminStatus.REJECTED) {
            this.rejectReason = rejectReason;
            this.rejectedAt = LocalDateTime.now();
        } else if (newStatus == AdminStatus.ACTIVATE) {
            this.activatedAt = LocalDateTime.now();
        }

        return this;
    }

    /**
     *  관리자 비밀번호 수정 기능
     */
    public Admin changePassword(String newPassword) {
        this.password = newPassword;
        return this;
    }
}
