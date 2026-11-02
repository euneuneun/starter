
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 고객 컨트롤러를 생성한다.
     *
     * @param customerService 고객 관리 서비스
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 고객 상세 정보를 조회한다.
     *
     * @param customerId 조회할 고객 ID
     * @return 고객 상세 정보와 HTTP 응답
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDetailResponse> detailCustomer(
            @PathVariable Long customerId) {

        CustomerDetailResponse responseDto =
                this.customerService.detailCustomer(customerId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 검색 및 필터 조건에 따라 고객 목록을 조회한다.
     *
     * @param keyword 고객 검색 키워드
     * @param page 조회할 페이지 번호
     * @param size 페이지당 고객 수
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param customerStatus 고객 상태 필터
     * @return 고객 목록과 페이징 정보 및 HTTP 응답
     */
    @GetMapping
    public ResponseEntity<CustomerListResponse> listCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(name = "status", required = false)
            Customer.CustomerStatus customerStatus) {

        CustomerListResponse responseDto =
                this.customerService.listCustomers(
                        keyword,
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        customerStatus);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 고객 정보를 수정한다.
     *
     * @param requestDto 고객 정보 수정 요청 데이터
     * @param customerId 수정할 고객 ID
     * @return 수정된 고객 정보와 HTTP 응답
     */
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerChangeResponse> changeCustomer(
            @RequestBody CustomerChangeRequest requestDto,
            @PathVariable Long customerId) {

        CustomerChangeResponse responseDto =
                this.customerService.changeCustomer(
                        requestDto, customerId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 고객 상태를 변경한다.
     *
     * @param requestDto 고객 상태 변경 요청 데이터
     * @param customerId 상태를 변경할 고객 ID
     * @return 변경된 고객 상태와 HTTP 응답
     */
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<CustomerStatusResponse> statusCustomer(
            @RequestBody CustomerStatusRequest requestDto,
            @PathVariable Long customerId) {

        CustomerStatusResponse responseDto =
                this.customerService.statusCustomer(
                        requestDto, customerId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 고객 정보를 삭제 처리한다.
     *
     * @param customerId 삭제할 고객 ID
     * @return 삭제 완료 메시지와 HTTP 응답
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Map<String, String>> deleteCustomer(
            @PathVariable Long customerId) {

        this.customerService.deleteCustomer(customerId);

        Map<String, String> responseBody = Map.of(
                "message",
                "요청하신 고객 정보가 삭제되었습니다.");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
