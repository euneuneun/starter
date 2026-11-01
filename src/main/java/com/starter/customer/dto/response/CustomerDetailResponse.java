
package com.starter.customer.dto.response;

import com.starter.customer.entity.Customer;
import java.time.LocalDateTime;

public class CustomerDetailResponse {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private Customer.CustomerStatus status;
    private LocalDateTime createdAt;
    /**
     * 고객 상세 조회 응답 객체를 생성한다.
     *
     * @param customerId 고객 ID
     * @param name 고객 이름
     * @param email 고객 이메일
     * @param phone 고객 전화번호
     * @param status 고객 상태
     * @param createdAt 고객 가입 일시
     */
    public CustomerDetailResponse(
            Long customerId,
            String name,
            String email,
            String phone,
            Customer.CustomerStatus status,
            LocalDateTime createdAt) {

        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
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
    public String getName() {
        return name;
    }
    /**
     * 고객 이메일을 반환한다.
     *
     * @return 고객 이메일
     */
    public String getEmail() {
        return email;
    }
    /**
     * 고객 전화번호를 반환한다.
     *
     * @return 고객 전화번호
     */
    public String getPhone() {
        return phone;
    }
    /**
     * 고객 상태를 반환한다.
     *
     * @return 고객 상태
     */
    public Customer.CustomerStatus getStatus() {
        return status;
    }
    /**
     * 고객 가입 일시를 반환한다.
     *
     * @return 고객 가입 일시
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
