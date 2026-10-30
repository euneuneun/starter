package com.starter.customer.service;

import com.starter.customer.dto.*;
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
import java.util.Optional;

@Service
public class CustomerService {

    // 속성
    private final CustomerRepository customerRepository;

    // 생성자
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 기능
    /**
     *  고객 상세 조회
     */
    @Transactional(readOnly = true)
    public CustomerDetailResponseDto detailCustomer(Long id) {
        // 1. id로 고객 조회
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);

        // 2. 고객 존재 여부 확인, 없으면 예외처리
        if(optionalCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Customer foundCustomer = optionalCustomer.get();

        // 4. entity -> dto 변환
        CustomerDetailResponseDto responseDto = new CustomerDetailResponseDto(
                foundCustomer.getId(), foundCustomer.getName(),foundCustomer.getEmail(), foundCustomer.getPhone(), foundCustomer.getStatus(), foundCustomer.getCreatedAt());

        // 5. 응답 반환
        return responseDto;
    }
    /**
     *  고객 리스트 조회
     */
    @Transactional(readOnly = true)
    public CustomerListResponseDto listCustomers(
            String keyword, Integer page, Integer size, String sortBy,
            String sortDirection, Customer.CustomerStatus status) {
        // 1. 검증
        // 1-1. page 검증
        if(page == null || page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        // 1-2. size 검증
        if(size == null || size < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        // 1-3. sortDirection 검증
        if(sortDirection == null ||
                (!sortDirection.equals("asc") && !sortDirection.equals("desc"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        // 1-4. sortBy 검증
        if(sortBy == null || (!sortBy.equals("name")
                && !sortBy.equals("email") && !sortBy.equals("createdAt"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        // 1-5. keyword 검증
        if(keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        // 1-6. status는 컨트롤러에서 enum으로 변환 -> 검증생략

        // 2. 정렬 방향 변환
        Sort.Direction direction = Sort.Direction.fromString(sortDirection); //"asc" / "desc" 문자열을 변환

        // 3. 정렬 조건 만들기
        Sort sort = Sort.by(direction,sortBy);

        // 4. 페이징 조건 만들기
        Pageable pageable = PageRequest.of(page-1, size, sort);

        // 5. 키워드 유무에 따라 고객 목록 조회
        Page<Customer> pageCustomer;

        if(keyword == null || keyword.isBlank()) {
            if(status == null) {
                pageCustomer = customerRepository.findAll(pageable);
            } else {
                pageCustomer = customerRepository.findByStatus(status, pageable);
            }
        } else {
            if(status == null) {
                pageCustomer = customerRepository.findByNameContainingOrEmailContaining(keyword, keyword, pageable);
            } else {
                pageCustomer = customerRepository.findByNameContainingAndStatusOrEmailContainingAndStatus(
                        keyword, status, keyword, status, pageable);
            }
        }

        // 6. entity -> dto 목록 변환
        List<CustomerDetailResponseDto> customerDtoList =
                pageCustomer.getContent().stream().map(customer -> new CustomerDetailResponseDto(
                        customer.getId(), customer.getName(), customer.getEmail(),
                        customer.getPhone(), customer.getStatus(), customer.getCreatedAt()
                )).toList();

        // 7. 응답 dto
        CustomerListResponseDto responseDto = new CustomerListResponseDto(
                customerDtoList, page, size, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());

        // 8. 응답 반환
        return responseDto;
    }
    /**
     *  고객 정보 수정
     */
    @Transactional
    public CustomerChangeResponseDto changeCustomer(CustomerChangeRequestDto requestDto, Long id) {

        // 1. id로 고객 조회
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);

        // 2. 고객 존재 여부 확인, 없으면 예외처리
        if(optionalCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Customer foundCustomer = optionalCustomer.get();

        // 4. RequestDto에서 변경할 정보 가져오기
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String phone = requestDto.getPhone();

        // 4-1. 가져온 정보 검증
        foundCustomer.changeCustomer(name, email, phone);

        // 4-2. 변경된 정보 db 동기화
        customerRepository.flush();

        // 5. entity -> dto 변환
        CustomerChangeResponseDto responseDto = new CustomerChangeResponseDto(
                foundCustomer.getId(), foundCustomer.getName(), foundCustomer.getEmail(), foundCustomer.getPhone(),
                foundCustomer.getStatus(), foundCustomer.getCreatedAt(), foundCustomer.getUpdatedAt());

        // 6. 응답 반환
        return responseDto;
    }
    /**
     *  고객 상태 변경
     */
    @Transactional
    public CustomerStatusResponseDto statusCustomer(CustomerStatusRequestDto requestDto, Long id) {

        // 1. id로 고객 조회
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);

        // 2. 고객 존재 여부 확인, 없으면 예외처리
        if(optionalCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Customer foundCustomer = optionalCustomer.get();

        // 4. RequestDto에서 변경할 정보 가져오기
        Customer.CustomerStatus status = requestDto.getStatus();

        // 4-1. 가져온 정보 검증
        try {
            foundCustomer.changeStatus(status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

        // 4-2. 변경된 정보 db 동기화
        customerRepository.flush();

        // 5. entity -> dto 변환
        CustomerStatusResponseDto responseDto = new CustomerStatusResponseDto(
                foundCustomer.getId(), foundCustomer.getStatus(), foundCustomer.getUpdatedAt());

        // 6. 응답 반환
        return responseDto;
    }
    /**
     *  고객 삭제
     */
    @Transactional
    public void deleteCustomer(Long id) {
        // 1. id로 고객 조회
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);

        // 2. 고객 존재 여부 확인, 없으면 예외처리
        if(optionalCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Customer foundCustomer = optionalCustomer.get();

        // 4. 논리 삭제 처리
        foundCustomer.deleteCustomer();
    }
}
