
package com.starter.customer.dto.response;

import com.starter.customer.enums.CustomerStatus;

import java.time.LocalDateTime;

public class CustomerStatusResponse {

    private Long customerId;
    private CustomerStatus customerStatus;
    private LocalDateTime updatedAt;

    /**
     * 고객 상태 변경 응답 객체를 생성한다.
     *
     * @param customerId 고객 ID
     * @param customerStatus 변경된 고객 상태
     * @param updatedAt 고객 상태 수정 일시
     */
    public CustomerStatusResponse(
            Long customerId,
            CustomerStatus customerStatus,
            LocalDateTime updatedAt) {

        this.customerId = customerId;
        this.customerStatus = customerStatus;
        this.updatedAt = updatedAt;
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
     * 고객 상태를 반환한다.
     *
     * @return 고객 상태
     */
    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    /**
     * 고객 상태 수정 일시를 반환한다.
     *
     * @return 고객 상태 수정 일시
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}