
package com.starter.customer.dto.request;

import com.starter.customer.entity.Customer;

public class CustomerStatusRequest {

    private Customer.CustomerStatus customerStatus;

    /**
     * 고객 상태 변경 요청 객체를 생성한다.
     */
    public CustomerStatusRequest() {
    }

    /**
     * 고객 상태 변경 요청 객체를 생성한다.
     *
     * @param customerStatus 변경할 고객 상태
     */
    public CustomerStatusRequest(
            Customer.CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * 고객 상태를 반환한다.
     *
     * @return 고객 상태
     */
    public Customer.CustomerStatus getCustomerStatus() {
        return customerStatus;
    }
}