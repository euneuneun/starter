package com.starter.product.controller;

import com.starter.product.dto.ProductCreateRequestDto;
import com.starter.product.dto.ProductCreateResponseDto;
import com.starter.product.dto.ProductDetailResponseDto;
import com.starter.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    /**
     *  상품 상세 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDto> detailProduct(@PathVariable Long id) {

        // 1. 서비스 처리
        ProductDetailResponseDto responseDto = this.productService.detailProduct(id);

        // 2. Http
        ResponseEntity<ProductDetailResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }

}
