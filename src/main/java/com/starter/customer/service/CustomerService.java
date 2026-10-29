package com.starter.customer.service;

import com.starter.customer.dto.CustomerDetailResponseDto;
import com.starter.customer.entity.Customer;
import com.starter.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
}
