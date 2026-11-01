package com.starter.admin.controller;

import com.starter.admin.exception.AdminException;
import com.starter.admin.dto.request.*;
import com.starter.admin.dto.response.*;
import com.starter.admin.enums.AdminErrorCode;
import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;
import com.starter.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 관리자 컨트롤러
 */
@RestController
@RequestMapping("/admins")
public class AdminController {
    //속성
    private final AdminService adminService;

    //생성자
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //기능

    /**
     * 관리자 회원가입 API
     *
     * @param request 관리자 회원가입 요청 DTO
     * @return 생성된 응답 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<AdminMessageResponse> create(@RequestBody @Valid AdminCreateRequest request) {
        // 1. 서비스 처리 - 회원 생성
        AdminMessageResponse createdResponse = this.adminService.createAdmin(request);

        // 2. 응답 생성
        ResponseEntity<AdminMessageResponse> response = new ResponseEntity<>(createdResponse, HttpStatus.CREATED);

        // 3. 반환
        return response;
    }

    /**
     * 관리자 로그인 API*
     *
     * @param loginRequest 로그인 요청 DTO
     * @param session      HTTP 세션
     * @return 로그인 성공 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AdminMessageResponse> login(
            @RequestBody @Valid AdminLoginRequest loginRequest,
            HttpSession session
    ) {
        // 1. 서비스 처리 - 로그인
        AdminMessageResponse loginResponse = this.adminService.loginAdmin(loginRequest, session);

        // 2. 세션 유효 시간 설정
        session.setMaxInactiveInterval(24 * 60 * 60);

        // 3. 응답 생성
        ResponseEntity<AdminMessageResponse> response = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        // 4. 반환
        return response;
    }

    /**
     * 관리자 로그아웃 API
     *
     * @param session HTTP 세션
     * @return 로그아웃 응답 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<AdminMessageResponse> logout(HttpSession session) {
        // 1. 서비스 처리 - 로그아웃
        AdminMessageResponse logoutResponse = this.adminService.logoutAdmin(session);

        // 2. 응답 생성
        ResponseEntity<AdminMessageResponse> response = new ResponseEntity<>(logoutResponse, HttpStatus.OK);

        // 3. 반환
        return response;
    }

    /**
     * 관리자 다건 조회 API
     *
     * @param keyword   검색 키워드
     * @param page      페이지 번호
     * @param limit     페이지 당 항목 수
     * @param sortBy    정렬 기준
     * @param sortOrder 정렬 방향
     * @param role      관리자 역할 필터
     * @param status    관리자 상태 필터
     * @param session   HTTP 세션
     * @return 관리자 목록 응답
     */
    @GetMapping
    public ResponseEntity<AdminListResponse> getAdminListAPI(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) AdminRole role,
            @RequestParam(required = false) AdminStatus status,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 요청 - 회원 목록 조회
        AdminListResponse adminListResponse = this.adminService.getAdminList(
                keyword,
                page,
                limit,
                sortBy,
                sortOrder,
                role,
                status,
                loginAdminId
        );

        // 4. 응답 생성
        ResponseEntity<AdminListResponse> response = new ResponseEntity<>(adminListResponse, HttpStatus.OK);

        // 5. 반환
        return response;

    }

    /**
     * 관리자 상세 조회 API
     *
     * @param adminId 조회할 관리자 ID
     * @param session HTTP 세션
     * @return 관리자 상세 정보 응답
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminGetInfoResponse> getAdminDetail(
            @PathVariable Long adminId,
            HttpSession session
    ) {

        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 관리자 상세 조회
        AdminGetInfoResponse adminGetInfoResponse = this.adminService.getAdminInfo(loginAdminId, adminId);

        // 4. 응답 생성
        ResponseEntity<AdminGetInfoResponse> response = new ResponseEntity<>(adminGetInfoResponse, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 관리자 정보 수정 API
     *
     * @param adminId                수정할 관리자 ID
     * @param adminUpdateInfoRequest 수정 정보 DTO
     * @param session                HTTP 세션
     * @return 수정된 관리자 정보 응답
     */
    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminUpdateInfoResponse> updateAdminInfo(
            @PathVariable Long adminId,
            @RequestBody AdminUpdateInfoRequest adminUpdateInfoRequest,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 관리자 정보 수정
        AdminUpdateInfoResponse updateInfoResponse = this.adminService.updateAdminInfo(
                loginAdminId,
                adminId,
                adminUpdateInfoRequest);

        // 4. 응답 생성
        ResponseEntity<AdminUpdateInfoResponse> response = new ResponseEntity<>(updateInfoResponse, HttpStatus.OK);

        // 5.반환
        return response;
    }

    /**
     * 관리자 역할 변경 API
     *
     * @param adminId                대상 관리자 ID
     * @param adminUpdateRoleRequest 역할 변경 요청 DTO
     * @param session                HTTP 세션
     * @return 역할 변경 결과 응답
     */
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<AdminUpdateRoleResponse> updateAmdinRole(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminUpdateRoleRequest adminUpdateRoleRequest,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 2. 서비스 처리 - 관리자 역할 수정
        AdminUpdateRoleResponse updateRoleResponse = this.adminService.updateAdminRole(
                loginAdminId,
                adminId,
                adminUpdateRoleRequest);

        // 3. 응답 생성
        ResponseEntity<AdminUpdateRoleResponse> response = new ResponseEntity<>(updateRoleResponse, HttpStatus.OK);

        // 4. 반환
        return response;
    }

    /**
     * 관리자 상태 변경 API
     *
     * @param adminId                  대상 관리자 ID
     * @param adminUpdateStatusRequest 상태 변경 요청 DTO
     * @param session                  HTTP 세션
     * @return 상태 변경 결과 응답
     */
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<AdminUpdateStatusResponse> updateAmdinStatus(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminUpdateStatusRequest adminUpdateStatusRequest,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 관리자 역할 수정
        AdminUpdateStatusResponse updateStatusResponse = this.adminService.updateAdminStatus(
                loginAdminId,
                adminId,
                adminUpdateStatusRequest);

        // 4. 응답 생성
        ResponseEntity<AdminUpdateStatusResponse> response = new ResponseEntity<>(updateStatusResponse, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 관리자 삭제 API
     *
     * @param adminId 삭제할 관리자 ID
     * @param session HTTP 세션
     * @return 삭제 완료 메시지 응답
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<AdminMessageResponse> deleteAdmin(@PathVariable Long adminId, HttpSession session) {

        // 1. 세션 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 관리자 삭제
        AdminMessageResponse messageResponse = this.adminService.deleteAdmin(loginAdminId, adminId);

        // 4. 생성 - ResponseEntity
        ResponseEntity<AdminMessageResponse> response = new ResponseEntity<>(messageResponse, HttpStatus.OK);

        // 5. 반환
        return response;
    }


    /**
     * 내 정보 조회 API
     *
     * @param session HTTP 세션
     * @return 내 정보 응답
     */
    @GetMapping("/me")
    public ResponseEntity<AdminGetMyInfoResponse> getAdminProfile(HttpSession session) {

        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 내 정보 조회
        AdminGetMyInfoResponse getMyInfoResponse = this.adminService.getAdminProfile(loginAdminId);

        // 4. 응답 생성
        ResponseEntity<AdminGetMyInfoResponse> response = new ResponseEntity<>(getMyInfoResponse, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 내 정보 수정 API
     *
     * @param adminUpdateInfoRequest 내 정보 수정 DTO
     * @param session                HTTP 세션
     * @return 수정된 내 정보 응답
     */
    @PatchMapping("/me")
    public ResponseEntity<AdminUpdateInfoResponse> updateAdminProfileInfo(
            @RequestBody @Valid AdminUpdateInfoRequest adminUpdateInfoRequest,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 내 정보 수정
        AdminUpdateInfoResponse updateInfoResponse = this.adminService.updateAdminProfileInfo(
                loginAdminId,
                adminUpdateInfoRequest
        );

        // 4. 생성 - ResponseEntity
        ResponseEntity<AdminUpdateInfoResponse> response = new ResponseEntity<>(updateInfoResponse, HttpStatus.OK);

        // 5.반환
        return response;

    }

    /**
     * 비밀번호 변경 API
     *
     * @param updatePasswordRequest 비밀번호 변경 DTO
     * @param session               HTTP 세션
     * @return 변경 완료 메시지 응답
     */
    @PatchMapping("/me/password")
    public ResponseEntity<AdminMessageResponse> updatePassword(
            @RequestBody @Valid AdminUpdatePasswordRequest updatePasswordRequest,
            HttpSession session
    ) {

        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 로그인 ID 공백 검증
        if (loginAdminId == null) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 3. 서비스 처리 - 비밀번호 변경 & 응답 메세지 생성
        AdminMessageResponse messageResponse = adminService.updatePassword(loginAdminId, updatePasswordRequest);

        // 4. 생성 - ResponseEntity
        ResponseEntity<AdminMessageResponse> response = new ResponseEntity<>(messageResponse, HttpStatus.OK);

        // 5. 반환
        return response;
    }


    /**
     * 예외 처리 메서드
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerRuntimeException(RuntimeException runtimeException) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 기본값 400
        String message = runtimeException.getMessage();

        if (runtimeException instanceof AdminException) {
            AdminException adminException = (AdminException) runtimeException;
            status = adminException.getErrorCode().getStatus();
            message = adminException.getMessage();
        }

        return ResponseEntity
                .status(status)
                .body("요청 오류: " + message);
    }

    /**
     * 예외 처리 2
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        // 발생한 모든 에러 메시지를 수집해서 이어붙이기
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", ")); // 쉼표로 구분

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청 오류: " + errorMessage);
    }
}