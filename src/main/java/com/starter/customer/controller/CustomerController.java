package com.starter.customer.controller;

import com.starter.customer.dto.CustomerDetailResponseDto;
import com.starter.customer.dto.CustomerListResponseDto;
import com.starter.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    // 속성
    private final CustomerService customerService;

    // 생성자
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 기능
    /**
     *  고객 상세 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailResponseDto> detailCustomer(@PathVariable Long id) {

        // 1. 서비스 처리
        CustomerDetailResponseDto responseDto = this.customerService.detailCustomer(id);

        // 2. Http
        ResponseEntity<CustomerDetailResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  고객 리스트 조회 API
     */
    @GetMapping
    public ResponseEntity<CustomerListResponseDto> listCustomers(
            @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String status) {

        // 1. 서비스 처리
        CustomerListResponseDto responseDto = this.customerService.listCustomers(
                keyword, page, size, sortBy, sortDirection, status);

        // 2. Http
        ResponseEntity<CustomerListResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }

}
