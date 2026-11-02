package com.starter.admin.enums;

/**
 * 관리자 상태 Enum
 */
public enum AdminStatus {
    PENDING,   // 승인 대기
    REJECTED,   // 반려됨
    ACTIVATE, // 계정 활성
    DEACTIVATE, //계정 비활성화
    SUSPENDED //계정 정지
}
