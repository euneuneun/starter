
package com.starter.customer.dto.response;

import com.starter.customer.entity.Customer;

import java.time.LocalDateTime;

public class CustomerStatusResponse {
    private Long customerId;
    private Customer.CustomerStatus status;
    private LocalDateTime updatedAt;
    /**
     * 고객 상태 변경 응답 객체를 생성한다.
     *
     * @param customerId 고객 ID
     * @param status 변경된 고객 상태
     * @param updatedAt 고객 상태 수정 일시
     */
    public CustomerStatusResponse(
            Long customerId,
            Customer.CustomerStatus status,
            LocalDateTime updatedAt) {

        this.customerId = customerId;
        this.status = status;
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
    public Customer.CustomerStatus getStatus() {
        return status;
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
