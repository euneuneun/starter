package com.starter.product.controller;

import com.starter.product.dto.*;
import com.starter.product.entity.Product;
import com.starter.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    /**
     *  상품 재고 변경 API
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductChangeStockResponseDto> changeStock(
            @RequestBody ProductChangeStockRequestDto requestDto, @PathVariable Long id) {
        System.out.println("상품 재고 변경 Controller 진입");
        // 1. 서비스 처리
        ProductChangeStockResponseDto responseDto = this.productService.changeStock(requestDto, id);

        // 2. Http
        ResponseEntity<ProductChangeStockResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 리스트 조회 API
     */
    @GetMapping
    public ResponseEntity<ProductListResponseDto> listProducts(
            @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection, @RequestParam(required = false) String category,
            @RequestParam(required = false) Product.ProductStatus status) {

        // 1. 서비스 처리
        ProductListResponseDto responseDto = this.productService.listProducts(
                keyword, page, size, sortBy, sortDirection, category, status);

        // 2. Http
        ResponseEntity<ProductListResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 정보 수정 API
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductChangeResponseDto> changeProduct(
            @RequestBody ProductChangeRequestDto requestDto, @PathVariable Long id) {

        // 1. 서비스 처리
        ProductChangeResponseDto responseDto = this.productService.changeProduct(requestDto, id);

        // 2. Http
        ResponseEntity<ProductChangeResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 상태 변경 API
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductStatusResponseDto> statusProduct(
            @RequestBody ProductStatusRequestDto requestDto, @PathVariable Long id) {

        // 1. 서비스처리
        ProductStatusResponseDto responseDto = this.productService.statusProduct(requestDto, id);

        // 2. Http
        ResponseEntity<ProductStatusResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 삭제 API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {

        // 1. 서비스 처리
        this.productService.deleteProduct(id);

        // 2. 메세지 전달
        Map<String, String> responseBody = Map.of("message", "요청하신 상품이 삭제되었습니다.");

        // 3. Http
        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // 4. 반환
        return response;
    }

}
