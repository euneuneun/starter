package com.starter.admin.dto.response;


/**
 * 관리자 메시지 응답 DTO
 */
public class AdminMessageResponse {
    //속성
    private final String message;

    //생성자
    public AdminMessageResponse(String message) {
        this.message = message;
    }

    //기능
    public String getMessage() {
        return message;
    }
}
