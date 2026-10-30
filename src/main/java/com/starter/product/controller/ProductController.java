package com.starter.product.controller;

import com.starter.product.dto.*;
import com.starter.product.entity.Product;
import com.starter.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestBody ProductCreateRequest createRequestDto, HttpSession session) {

        // 1. 관리자 id 가져오기
        Long adminId = (Long) session.getAttribute("ADMIN_ID");

        // 1-1. 로그인 여부 검증
        if(adminId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }

        // 2. 서비스 처리
        ProductCreateResponse responseDto = this.productService.createProduct(createRequestDto, adminId);

        // 3. Http
        ResponseEntity<ProductCreateResponse> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        // 4. 반환
        return response;
    }
    /**
     *  상품 상세 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> detailProduct(@PathVariable Long id) {

        // 1. 서비스 처리
        ProductDetailResponse responseDto = this.productService.detailProduct(id);

        // 2. Http
        ResponseEntity<ProductDetailResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 재고 변경 API
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductChangeStockResponse> changeStock(
            @RequestBody ProductChangeStockRequest requestDto, @PathVariable Long id) {
        System.out.println("상품 재고 변경 Controller 진입");
        // 1. 서비스 처리
        ProductChangeStockResponse responseDto = this.productService.changeStock(requestDto, id);

        // 2. Http
        ResponseEntity<ProductChangeStockResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 리스트 조회 API
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> listProducts(
            @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection, @RequestParam(required = false) String category,
            @RequestParam(required = false) Product.ProductStatus status) {

        // 1. 서비스 처리
        ProductListResponse responseDto = this.productService.listProducts(
                keyword, page, size, sortBy, sortDirection, category, status);

        // 2. Http
        ResponseEntity<ProductListResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 정보 수정 API
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductChangeResponse> changeProduct(
            @RequestBody ProductChangeRequest requestDto, @PathVariable Long id) {

        // 1. 서비스 처리
        ProductChangeResponse responseDto = this.productService.changeProduct(requestDto, id);

        // 2. Http
        ResponseEntity<ProductChangeResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 3. 반환
        return response;
    }
    /**
     *  상품 상태 변경 API
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductStatusResponse> statusProduct(
            @RequestBody ProductStatusRequest requestDto, @PathVariable Long id) {

        // 1. 서비스처리
        ProductStatusResponse responseDto = this.productService.statusProduct(requestDto, id);

        // 2. Http
        ResponseEntity<ProductStatusResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

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
