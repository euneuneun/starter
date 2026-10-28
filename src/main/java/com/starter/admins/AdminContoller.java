package com.starter.admins;

import com.starter.admins.dto.requestDto.AdminCreateRequestDto;
import com.starter.admins.dto.responseDto.AdminResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 관리자 생성 API
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
}
