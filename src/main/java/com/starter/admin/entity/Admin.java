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
    private String adminName;

    @Column(nullable = false, length = 20)
    private String adminEmail;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String adminPhone;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AdminRole role;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AdminStatus adminStatus;

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
            String adminName,
            String adminEmail,
            String password,
            String adminPhone,
            AdminRole role
    ) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.password = password;
        this.adminPhone = adminPhone;
        this.role = role;
        this.adminStatus = AdminStatus.PENDING;
    }

    //기능
    /**
     *  Getter
     */
    public Long getAdminId() {
        return adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public AdminRole getRole() {
        return role;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
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
    public Admin changeInfo(String newAdminName, String newAdminEmail, String newAdminPhone) {
        this.adminName = newAdminName;
        this.adminEmail = newAdminEmail;
        this.adminPhone = newAdminPhone;
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
        this.adminStatus = newStatus;

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
