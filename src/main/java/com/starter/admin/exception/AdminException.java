package com.starter.admin.exception;

import com.starter.admin.enums.AdminErrorCode;

/**
 * 관리자 에러 Exception
 */
public class AdminException extends RuntimeException {
    //속성
    private final AdminErrorCode errorCode;

    //생성자
    public AdminException(AdminErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 에러 코드 & 거절 사유
    public AdminException(AdminErrorCode errorCode, String reason) {
        super(errorCode.getMessage() + " 거절 사유:" + reason);
        this.errorCode = errorCode;
    }

    //기능
    public AdminErrorCode getErrorCode() {
        return errorCode;
    }
}