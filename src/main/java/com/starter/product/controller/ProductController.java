package com.starter.product.controller;

import com.starter.product.dto.ProductCreateRequestDto;
import com.starter.product.dto.ProductCreateResponseDto;
import com.starter.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    // 속성
    private final ProductService productService;

    // 생성자
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 기능
    /**
     *  상품등록 API
     */
    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> createProduct(@RequestBody ProductCreateRequestDto createRequestDto) {

        // 1. 서비스 처리
        ProductCreateResponseDto responseDto = this.productService.createProduct(createRequestDto);

        // 2. Http
        ResponseEntity<ProductCreateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        // 3. 반환
        return response;
    }

}
