
package com.starter.customer.dto.request;

public class CustomerChangeRequest {
    private String name;
    private String email;
    private String phone;
    /**
     * 고객 정보 수정 요청 객체를 생성한다.
     */
    public CustomerChangeRequest() {
    }
    /**
     * 고객 정보 수정 요청 객체를 생성한다.
     *
     * @param name 수정할 고객 이름
     * @param email 수정할 고객 이메일
     * @param phone 수정할 고객 전화번호
     */
    public CustomerChangeRequest(
            String name,
            String email,
            String phone) {

        this.name = name;
        this.email = email;
        this.phone = phone;
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
}
