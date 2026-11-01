
package com.starter.customer.dto.request;

public class CustomerChangeRequest {

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    /**
     * 고객 정보 수정 요청 객체를 생성한다.
     */
    public CustomerChangeRequest() {
    }
    /**
     * 고객 정보 수정 요청 객체를 생성한다.
     *
     * @param customerName 수정할 고객 이름
     * @param customerEmail 수정할 고객 이메일
     * @param customerPhone 수정할 고객 전화번호
     */
    public CustomerChangeRequest(
            String customerName,
            String customerEmail,
            String customerPhone) {

        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
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
}