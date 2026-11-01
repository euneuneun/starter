
package com.starter.customer.entity;

import com.starter.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    /**
     * 고객 상태를 정의한다.
     */
    public enum CustomerStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String customerEmail;

    @Column(name = "phone", nullable = true, length = 15)
    private String customerPhone;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private CustomerStatus customerStatus;
    /**
     * JPA에서 사용하는 기본 생성자이다.
     */
    protected Customer() {
    }
    /**
     * 고객 정보를 수정한다.
     *
     * @param customerName 변경할 고객 이름
     * @param customerEmail 변경할 고객 이메일
     * @param customerPhone 변경할 고객 전화번호
     * @throws IllegalArgumentException 유효하지 않은 고객 정보
     */
    public void changeCustomer(
            String customerName,
            String customerEmail,
            String customerPhone) {

        if (customerName != null) {
            if (customerName.isBlank() || customerName.length() > 100) {
                throw new IllegalArgumentException("이름을 확인해주세요.");
            }

            this.customerName = customerName;
        }

        if (customerEmail != null) {
            if (customerEmail.isBlank() || customerEmail.length() > 255) {
                throw new IllegalArgumentException("이메일을 확인해주세요.");
            }

            this.customerEmail = customerEmail;
        }

        if (customerPhone != null) {
            if (customerPhone.isBlank() || customerPhone.length() > 15) {
                throw new IllegalArgumentException("전화번호를 확인해주세요.");
            }

            this.customerPhone = customerPhone;
        }
    }
    /**
     * 고객 상태를 변경한다.
     *
     * @param customerStatus 변경할 고객 상태
     * @throws IllegalArgumentException 고객 상태가 null인 경우
     */
    public void changeCustomerStatus(CustomerStatus customerStatus) {

        if (customerStatus == null) {
            throw new IllegalArgumentException("상태값이 비어있습니다.");
        }

        this.customerStatus = customerStatus;
    }
    /**
     * 고객 정보를 논리적으로 삭제한다.
     */
    public void deleteCustomer() {
        super.softDelete();
    }
    /**
     * 고객 ID를 반환한다.
     *
     * @return 고객 ID
     */
    public Long getCustomerId() {
        return customerId;
    }
    /**
     * 고객 이름을 반환한다.
     *
     * @return 고객 이름
     */
    public String getCustomerName() {
        return customerName;
    }
    /**
     * 고객 이메일을 반환한다.
     *
     * @return 고객 이메일
     */
    public String getCustomerEmail() {
        return customerEmail;
    }
    /**
     * 고객 전화번호를 반환한다.
     *
     * @return 고객 전화번호
     */
    public String getCustomerPhone() {
        return customerPhone;
    }
    /**
     * 고객 상태를 반환한다.
     *
     * @return 고객 상태
     */
    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }
}