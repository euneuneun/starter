package com.starter.admins.contoller;

import com.starter.admins.dto.requestDto.AdminCreateRequestDto;
import com.starter.admins.dto.requestDto.AdminLoginRequestDto;
import com.starter.admins.dto.responseDto.AdminListResponseDto;
import com.starter.admins.dto.responseDto.AdminLoginResponseDto;
import com.starter.admins.dto.responseDto.AdminResponseDto;
import com.starter.admins.dto.service.AdminService;
import com.starter.admins.entity.Admin;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<AdminResponseDto> create(@RequestBody @Valid AdminCreateRequestDto requestDto) {

        // 1. 서비스 처리 - 회원 생성
        AdminResponseDto responseDto = this.adminService.createAdmin(requestDto);

        // 2. 생성 - ResponseEntity
        ResponseEntity<AdminResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

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
            @RequestParam(required = false) Admin.AdminStatus status
    ) {

        // 1. 서비스 처리 요청 - 회원 목록 조회
        AdminListResponseDto responseDto = this.adminService.getAdminList(
                keyword,
                page,
                limit,
                sortBy,
                sortOrder,
                role,
                status
        );

        // 2. 응답 생성
        ResponseEntity<AdminListResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
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