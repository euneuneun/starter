package com.starter.admin.enums;

import org.springframework.http.HttpStatus;

/**
 * 관리자 에러코드 Enum
 */
public enum AdminErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "Unauthorized user"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "Permission Denied"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Found"),
    ALREADY_EXISTS(HttpStatus.CONFLICT, "Already exists");

    //속성
    private final HttpStatus status;
    private final String message;

    //생성자
    AdminErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    //기능
    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
