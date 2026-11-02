
package com.starter.customer.dto.response;

import com.starter.customer.enums.CustomerStatus;
import java.time.LocalDateTime;

public class CustomerDetailResponse {

    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private CustomerStatus customerStatus;
    private LocalDateTime createdAt;

    /**
     * 고객 상세 조회 응답 객체를 생성한다.
     *
     * @param customerId 고객 ID
     * @param customerName 고객 이름
     * @param customerEmail 고객 이메일
     * @param customerPhone 고객 전화번호
     * @param customerStatus 고객 상태
     * @param createdAt 고객 가입 일시
     */
    public CustomerDetailResponse(
            Long customerId,
            String customerName,
            String customerEmail,
            String customerPhone,
            CustomerStatus customerStatus,
            LocalDateTime createdAt) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerStatus = customerStatus;
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

    /**
     * 고객 가입 일시를 반환한다.
     *
     * @return 고객 가입 일시
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}