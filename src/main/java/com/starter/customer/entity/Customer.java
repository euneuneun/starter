package com.starter.customer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    // Enum 정의
    // 고객 상태
    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = true, length = 15)
    private String phone;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private CustomerStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = true)
    private LocalDateTime deletedAt;


    // 생성자
    // JPA 기본 생성자
    protected Customer() {
    }


    // 기능
    // 고객 정보 수정
    public void changeCustomer(String name, String email, String phone) {
        if(name == null || name.isBlank() || name.length() > 100) {
            throw new IllegalArgumentException("이름은 null 이거나 100자를 넘을 수 없습니다.");
        }
        if(email == null || email.isBlank() || email.length() > 255) {
            throw new IllegalArgumentException("이메일은 null 이거나 255자를 넘을 수 없습니다.");
        }
        if(phone != null && phone.length() > 15) {
            throw new IllegalArgumentException("전화번호는 15자를 넘을 수 없습니다.");
        }
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    // 고객 상태 변경
    public void changeStatus(CustomerStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("상태값이 비어있습니다.");
        } else {
            this.status=status;
        }
    }
    // 고객 삭제 처리
    public void deleteCustomer() {
        this.deletedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public CustomerStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

}
