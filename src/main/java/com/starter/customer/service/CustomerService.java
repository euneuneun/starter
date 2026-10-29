package com.starter.customer.service;

import com.starter.customer.dto.CustomerDetailResponseDto;
import com.starter.customer.dto.CustomerListResponseDto;
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
            String keyword, Integer page, Integer size, String sortBy, String sortDirection, String status) {

        // 1. 정렬 방향 변환
        Sort.Direction direction = Sort.Direction.fromString(sortDirection); //"asc" / "desc" 문자열을 변환

        // 2. 정렬 조건 만들기
        Sort sort = Sort.by(direction,sortBy);

        // 3. 페이징 조건 만들기
        Pageable pageable = PageRequest.of(page-1, size, sort);

        // 4. 키워드 유무에 따라 고객 목록 조회
        Page<Customer> pageCustomer;

        if(keyword == null || keyword.isBlank()) {
            if(status == null || status.isBlank()) {
                pageCustomer = customerRepository.findAll(pageable);
            } else {
                pageCustomer = customerRepository.findByStatus(Customer.CustomerStatus.valueOf(status), pageable);
            }
        } else {
            if(status == null || status.isBlank()) {
                pageCustomer = customerRepository.findByNameContainingOrEmailContaining(keyword, keyword, pageable);
            } else {
                pageCustomer = customerRepository.findByNameContainingAndStatusOrEmailContainingAndStatus(
                        keyword,Customer.CustomerStatus.valueOf(status),
                        keyword, Customer.CustomerStatus.valueOf(status), pageable);
            }
        }

        // 5. entity -> dto 목록 변환
        List<CustomerDetailResponseDto> customerDtoList =
                pageCustomer.getContent().stream().map(customer -> new CustomerDetailResponseDto(
                        customer.getId(), customer.getName(), customer.getEmail(),
                        customer.getPhone(), customer.getStatus(), customer.getCreatedAt()
                )).toList();

        // 6. 응답 dto
        CustomerListResponseDto responseDto = new CustomerListResponseDto(
                customerDtoList, page, size, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());

        // 7. 응답 반환
        return responseDto;

    }
}
