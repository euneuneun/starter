
package com.starter.customer.service;

import com.starter.customer.dto.request.CustomerChangeRequest;
import com.starter.customer.dto.request.CustomerStatusRequest;
import com.starter.customer.dto.response.CustomerChangeResponse;
import com.starter.customer.dto.response.CustomerDetailResponse;
import com.starter.customer.dto.response.CustomerListResponse;
import com.starter.customer.dto.response.CustomerStatusResponse;
import com.starter.customer.entity.Customer;
import com.starter.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 서비스를 생성한다.
     *
     * @param customerRepository 고객 저장소
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 고객 상세 정보를 조회한다.
     *
     * @param customerId 조회할 고객 ID
     * @return 고객 상세 정보
     * @throws ResponseStatusException 고객이 없거나 삭제된 경우
     */
    @Transactional(readOnly = true)
    public CustomerDetailResponse detailCustomer(Long customerId) {

        // 1. 고객 ID로 고객 조회 및 존재 여부 확인
        Customer foundCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource Not Found"));

        // 2. 삭제된 고객인지 확인
        if (foundCustomer.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Resource Not Found");
        }

        // 3. Entity -> DTO 변환
        CustomerDetailResponse responseDto = new CustomerDetailResponse(
                foundCustomer.getCustomerId(),
                foundCustomer.getCustomerName(),
                foundCustomer.getCustomerEmail(),
                foundCustomer.getCustomerPhone(),
                foundCustomer.getCustomerStatus(),
                foundCustomer.getCreatedAt());

        // 4. 응답 반환
        return responseDto;
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
     * @return 고객 목록과 페이징 정보
     * @throws ResponseStatusException 조회 조건이 잘못된 경우
     */
    @Transactional(readOnly = true)
    public CustomerListResponse listCustomers(
            String keyword,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            Customer.CustomerStatus customerStatus) {

        // 1. page 검증
        if (page == null || page < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid request");
        }

        // 2. size 검증
        if (size == null || size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid request");
        }

        // 3. sortDirection 검증
        if (sortDirection == null
                || (!sortDirection.equals("asc")
                && !sortDirection.equals("desc"))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid request");
        }

        // 4. sortBy 검증
        if (sortBy == null
                || (!sortBy.equals("name")
                && !sortBy.equals("email")
                && !sortBy.equals("createdAt"))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid request");
        }

        // 5. keyword 검증
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        // 6. 정렬 방향 변환
        Sort.Direction direction =
                Sort.Direction.fromString(sortDirection);

        // 7. API 정렬 기준을 Entity 필드명으로 변환
        String sortProperty = switch (sortBy) {
            case "name" -> "customerName";
            case "email" -> "customerEmail";
            default -> "createdAt";
        };

        // 8. 정렬 조건 생성
        Sort sort = Sort.by(direction, sortProperty);

        // 9. 페이징 조건 생성
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 10. 검색 조건에 따라 고객 목록 조회
        Page<Customer> pageCustomer =
                customerRepository.searchCustomers(
                        keyword, customerStatus, pageable);

        // 11. Entity -> DTO 목록 변환
        List<CustomerDetailResponse> customerDtoList =
                pageCustomer.getContent()
                        .stream()
                        .map(customer -> new CustomerDetailResponse(
                                customer.getCustomerId(),
                                customer.getCustomerName(),
                                customer.getCustomerEmail(),
                                customer.getCustomerPhone(),
                                customer.getCustomerStatus(),
                                customer.getCreatedAt()))
                        .toList();

        // 12. 응답 DTO 생성
        CustomerListResponse responseDto = new CustomerListResponse(
                customerDtoList,
                page,
                size,
                pageCustomer.getTotalElements(),
                pageCustomer.getTotalPages());

        // 13. 응답 반환
        return responseDto;
    }

    /**
     * 고객 기본 정보를 수정한다.
     *
     * @param requestDto 고객 정보 수정 요청 데이터
     * @param customerId 수정할 고객 ID
     * @return 수정된 고객 정보
     * @throws ResponseStatusException 고객이 없거나 삭제된 경우
     */
    @Transactional
    public CustomerChangeResponse changeCustomer(
            CustomerChangeRequest requestDto,
            Long customerId) {

        // 1. 고객 ID로 고객 조회 및 존재 여부 확인
        Customer foundCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource Not Found"));

        // 2. 삭제된 고객인지 확인
        if (foundCustomer.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Resource Not Found");
        }

        // 3. Request DTO에서 변경할 정보 가져오기
        String customerName = requestDto.getCustomerName();
        String customerEmail = requestDto.getCustomerEmail();
        String customerPhone = requestDto.getCustomerPhone();

        // 4. 고객 정보 수정
        foundCustomer.changeCustomer(
                customerName,
                customerEmail,
                customerPhone);

        // 5. 변경된 정보 DB 동기화
        customerRepository.flush();

        // 6. Entity -> DTO 변환
        CustomerChangeResponse responseDto = new CustomerChangeResponse(
                foundCustomer.getCustomerId(),
                foundCustomer.getCustomerName(),
                foundCustomer.getCustomerEmail(),
                foundCustomer.getCustomerPhone(),
                foundCustomer.getCustomerStatus(),
                foundCustomer.getCreatedAt(),
                foundCustomer.getUpdatedAt());

        // 7. 응답 반환
        return responseDto;
    }

    /**
     * 고객 상태를 변경한다.
     *
     * @param requestDto 고객 상태 변경 요청 데이터
     * @param customerId 상태를 변경할 고객 ID
     * @return 변경된 고객 상태 정보
     * @throws ResponseStatusException 고객이 없거나 요청이 잘못된 경우
     */
    @Transactional
    public CustomerStatusResponse statusCustomer(
            CustomerStatusRequest requestDto,
            Long customerId) {

        // 1. 고객 ID로 고객 조회 및 존재 여부 확인
        Customer foundCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource Not Found"));

        // 2. 삭제된 고객인지 확인
        if (foundCustomer.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Resource Not Found");
        }

        // 3. Request DTO에서 변경할 상태 가져오기
        Customer.CustomerStatus customerStatus =
                requestDto.getCustomerStatus();

        // 4. 고객 상태 변경 및 예외 처리
        try {
            foundCustomer.changeCustomerStatus(customerStatus);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid request");
        }

        // 5. 변경된 정보 DB 동기화
        customerRepository.flush();

        // 6. Entity -> DTO 변환
        CustomerStatusResponse responseDto = new CustomerStatusResponse(
                foundCustomer.getCustomerId(),
                foundCustomer.getCustomerStatus(),
                foundCustomer.getUpdatedAt());

        // 7. 응답 반환
        return responseDto;
    }

    /**
     * 고객 정보를 논리적으로 삭제한다.
     *
     * @param customerId 삭제할 고객 ID
     * @throws ResponseStatusException 고객이 없거나 이미 삭제된 경우
     */
    @Transactional
    public void deleteCustomer(Long customerId) {

        // 1. 고객 ID로 고객 조회 및 존재 여부 확인
        Customer foundCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource Not Found"));

        // 2. 삭제된 고객인지 확인
        if (foundCustomer.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Resource Not Found");
        }

        // 3. 논리 삭제 처리
        foundCustomer.deleteCustomer();
    }
}