package com.starter.customer.entity;

import com.starter.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

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


    // 생성자
    // JPA 기본 생성자
    protected Customer() {
    }


    // 기능
    // 고객 정보 수정
    public void changeCustomer(String name, String email, String phone) {
        if(name != null) {
            if(name.isBlank() || name.length() > 100) {
                throw new IllegalArgumentException("이름을 확인해주세요.");
            }
            this.name = name;
        }
        if(email != null) {
            if(email.isBlank() || email.length() > 255) {
                throw new IllegalArgumentException("이메일을 확인해주세요.");
            }
            this.email = email;
        }
        if(phone != null) {
            if(phone.isBlank() || phone.length() > 15) {
                throw new IllegalArgumentException("전화번호를 확인해주세요.");
            }
            this.phone = phone;
        }
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
        super.softDelete();
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

}
