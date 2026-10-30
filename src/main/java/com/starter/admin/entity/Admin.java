package com.starter.admin.entity;

import com.starter.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "admins")
@SQLRestriction("deleted_at IS NULL")
public class Admin extends BaseEntity {

    //Enum정의
    // 관리자 역할
    public enum AdminRole {
        SUPER_ADMIN, // 슈퍼 관리자
        OPERATION_ADMIN, // 운영 관리자
        CS_ADMIN // CS관리자
    }

    //관리자 상태
    public enum AdminStatus {
        PENDING,   // 승인 대기
        REJECTED,   // 반려됨
        ACTIVATE, // 계정 활성
        DEACTIVATE, //계정 비활성화
        SUSPENDED //계정 정지

    }

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

    public Admin changeStatus(AdminStatus newStatus,String rejectReason) {
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
