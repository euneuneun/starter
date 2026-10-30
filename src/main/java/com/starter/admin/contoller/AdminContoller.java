package com.starter.admin.contoller;

import com.starter.admin.dto.requestDto.*;
import com.starter.admin.dto.responseDto.*;
import com.starter.admin.service.AdminService;
import com.starter.admin.entity.Admin;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("admins")
public class AdminContoller {

    //속성
    private final AdminService adminService;

    //생성자
    public AdminContoller(AdminService adminService) {
        this.adminService = adminService;
    }

    //기능

    /**
     * 관리자 회원가입 API
     */

    @PostMapping("/signup")
    public ResponseEntity<AdminMessageResponseDto> create(@RequestBody @Valid AdminCreateRequestDto requestDto) {

        // 1. 서비스 처리 - 회원 생성
        AdminMessageResponseDto responseDto = this.adminService.createAdmin(requestDto);

        // 2. 생성 - ResponseEntity
        ResponseEntity<AdminMessageResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        // 3. 반환
        return response;
    }

    /**
     * 관리자 로그인 API
     */

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(
            @RequestBody @Valid AdminLoginRequestDto requestDto,
            HttpSession session
    ) {
        // 1. 서비스 처리 - 로그인
        AdminLoginResponseDto responseDto = adminService.loginAdmin(requestDto, session);

        // 2. 세션 유효 시간 설정
        session.setMaxInactiveInterval(24 * 60 * 60);

        // 3. 생성 - ResponseEntity
        ResponseEntity<AdminLoginResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4. 반환
        return response;
    }

    /**
     * 관리자 로그아웃 API
     */

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 1. 서비스 처리 - 로그아웃
        adminService.logoutAdmin(session);

        // 2. 생성 ResponseEntity
        ResponseEntity<String> response = new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);

        // 3. 반환
        return response;
    }

    /**
     * 관리자 다건 조회 API
     */

    @GetMapping
    public ResponseEntity<AdminListResponseDto> getAdminListAPI(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) Admin.AdminRole role,
            @RequestParam(required = false) Admin.AdminStatus status,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 1. 서비스 처리 요청 - 회원 목록 조회
        AdminListResponseDto responseDto = this.adminService.getAdminList(
                keyword,
                page,
                limit,
                sortBy,
                sortOrder,
                role,
                status,
                loginAdminId
        );

        // 2. 응답 생성
        ResponseEntity<AdminListResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        // 3. 반환
        return response;

    }

    /**
     * 관리자 상세 조회 API
     */

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminGetDeatilResponseDto> getAdminDetail(
            @PathVariable Long adminId,
            HttpSession session
            ) {

        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 서비스 처리 - 관리자 상세 조회
        AdminGetDeatilResponseDto responseDto = this.adminService.getAdminDetail(loginAdminId, adminId);

        // 3. 응답 생성
        ResponseEntity<AdminGetDeatilResponseDto> result = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4. 반환
        return result;
    }

    /**
     * 관리자 정보 수정 API
     */

    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminUpdateResponseDto> updateAdminInfo(
            @PathVariable Long adminId,
            @RequestBody AdminUpdateInfoRequestDto adminUpdateInfoRequestDto,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 서비스 처리 - 관리자 정보 수정
        AdminUpdateResponseDto responseDto = this.adminService.updateAdminInfo(
                loginAdminId,
                adminId,
                adminUpdateInfoRequestDto);

        // 3. 응답 생성
        ResponseEntity<AdminUpdateResponseDto> result = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4.반환
        return result;

    }

    /**
     * 관리자 역할 변경 API
     */
    @PatchMapping("{adminId}/role")
    public ResponseEntity<AdminUpdateRoleResponseDto> updateAminRole(
            @PathVariable Long adminId,
            @RequestBody AdminUpdateRoleRequestDto adminUpdateRoleRequestDto,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 서비스 처리 - 관리자 역할 수정
        AdminUpdateRoleResponseDto responseDto = this.adminService.updateAdminRole(
                loginAdminId,
                adminId,
                adminUpdateRoleRequestDto);

        // 3. 응답 생성
        ResponseEntity<AdminUpdateRoleResponseDto> result = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4. 반환
        return result;
    }

    /**
     * 관리자 상태 변경
     */
    @PatchMapping("{adminId}/status")
    public ResponseEntity<AdminUpdateStatusResponseDto> updateAminRole(
            @PathVariable Long adminId,
            @RequestBody AdminUpdateStatusRequestDto adminUpdateStatusRequestDto,
            HttpSession session
    ) {
        // 1. 세션에 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 서비스 처리 - 관리자 역할 수정
        AdminUpdateStatusResponseDto responseDto = this.adminService.updateAdminStatus(
                loginAdminId,
                adminId,
                adminUpdateStatusRequestDto);

        // 3. 응답 생성
        ResponseEntity<AdminUpdateStatusResponseDto> result = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4. 반환
        return result;
    }


    /**
     * 관리자 삭제 APU
     */
    @DeleteMapping("{adminId}")
    public ResponseEntity<AdminMessageResponseDto>deleteAdmin(@PathVariable Long adminId, HttpSession session) {

        // 1. 세션 로그인한 관리자 ID 추출
        Long loginAdminId = (Long) session.getAttribute("ADMIN_ID");

        // 2. 서비스 처리
        AdminMessageResponseDto responseDto = this.adminService.deleteAdmin(loginAdminId, adminId);

        // 3. 생성 - ResponseEntity
        ResponseEntity<AdminMessageResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 4. 반환
        return response;
    }




    /**
     * 예외 처리 메서드
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청 오류: " + e.getMessage());
    }
}