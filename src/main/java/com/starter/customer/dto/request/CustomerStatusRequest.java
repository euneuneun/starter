
package com.starter.customer.dto.request;

import com.starter.customer.entity.Customer;

public class CustomerStatusRequest {
    private Customer.CustomerStatus status;
    /**
     * 고객 상태 변경 요청 객체를 생성한다.
     */
    public CustomerStatusRequest() {
    }
    /**
     * 고객 상태 변경 요청 객체를 생성한다.
     *
     * @param status 변경할 고객 상태
     */
    public CustomerStatusRequest(Customer.CustomerStatus status) {
        this.status = status;
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
     * 고객 상태를 설정한다.
     *
     * @param status 변경할 고객 상태
     */
    public void setStatus(Customer.CustomerStatus status) {
        this.status = status;
    }
}
