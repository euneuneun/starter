package com.starter.customer.controller;

import com.starter.customer.dto.request.CustomerChangeRequest;
import com.starter.customer.dto.request.CustomerStatusRequest;
import com.starter.customer.dto.response.CustomerChangeResponse;
import com.starter.customer.dto.response.CustomerDetailResponse;
import com.starter.customer.dto.response.CustomerListResponse;
import com.starter.customer.dto.response.CustomerStatusResponse;
import com.starter.customer.entity.Customer;
import com.starter.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<CustomerDetailResponse> detailCustomer(@PathVariable Long id) {

        // 1. 서비스 처리
        CustomerDetailResponse responseDto = this.customerService.detailCustomer(id);

        // 2. Http
        ResponseEntity<CustomerDetailResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  고객 리스트 조회 API
     */
    @GetMapping
    public ResponseEntity<CustomerListResponse> listCustomers(
            @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Customer.CustomerStatus status) {

        // 1. 서비스 처리
        CustomerListResponse responseDto = this.customerService.listCustomers(
                keyword, page, size, sortBy, sortDirection, status);

        // 2. Http
        ResponseEntity<CustomerListResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  고객 정보 수정 API
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerChangeResponse> changeCustomer(
            @RequestBody CustomerChangeRequest requestDto, @PathVariable Long id) {

        // 1. 서비스 처리
        CustomerChangeResponse responseDto = this.customerService.changeCustomer(requestDto, id);

        // 2. Http
        ResponseEntity<CustomerChangeResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  고객 상태 변경 API
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerStatusResponse> statusCustomer(
            @RequestBody CustomerStatusRequest requestDto, @PathVariable Long id) {

        // 1. 서비스 처리
        CustomerStatusResponse responseDto = this.customerService.statusCustomer(requestDto, id);

        // 2. Http
        ResponseEntity<CustomerStatusResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  고객 삭제 API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Long id) {

        // 1. 서비스 처리
        this.customerService.deleteCustomer(id);

        // 2. 메세지 전달
        Map<String, String> responseBody = Map.of("message", "요청하신 고객 정보가 삭제되었습니다.");

        // 3. Http
        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // 4. 반환
        return response;
    }

}
