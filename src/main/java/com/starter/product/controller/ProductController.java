
package com.starter.product.controller;

import com.starter.product.dto.request.ProductChangeRequest;
import com.starter.product.dto.request.ProductChangeStockRequest;
import com.starter.product.dto.request.ProductCreateRequest;
import com.starter.product.dto.request.ProductStatusRequest;
import com.starter.product.dto.response.*;
import com.starter.product.entity.Product;
import com.starter.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품을 등록한다.
     *
     * @param createRequestDto 상품 등록 요청 데이터
     * @param request HTTP 요청
     * @return 등록된 상품 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestBody ProductCreateRequest createRequestDto,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 관리자 ID 가져오기
        Long adminId = (Long) session.getAttribute("ADMIN_ID");

        // 4. 서비스 처리
        ProductCreateResponse responseDto =
                productService.createProduct(createRequestDto, adminId);

        // 5. HTTP 응답 생성
        ResponseEntity<ProductCreateResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        // 6. 반환
        return response;
    }

    /**
     * 상품 상세 정보를 조회한다.
     *
     * @param productId 조회할 상품 ID
     * @param request HTTP 요청
     * @return 상품 상세 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> detailProduct(
            @PathVariable("id") Long productId,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        ProductDetailResponse responseDto =
                productService.detailProduct(productId);

        // 4. HTTP 응답 생성
        ResponseEntity<ProductDetailResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 상품 재고를 변경한다.
     *
     * @param requestDto 상품 재고 변경 요청 데이터
     * @param productId 변경할 상품 ID
     * @param request HTTP 요청
     * @return 변경된 상품 재고 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductChangeStockResponse> changeProductStock(
            @RequestBody ProductChangeStockRequest requestDto,
            @PathVariable("id") Long productId,
            HttpServletRequest request) {

        System.out.println("상품 재고 변경 Controller 진입");

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        ProductChangeStockResponse responseDto =
                productService.changeStock(requestDto, productId);

        // 4. HTTP 응답 생성
        ResponseEntity<ProductChangeStockResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 상품 목록을 조회한다.
     *
     * @param keyword 상품 검색 키워드
     * @param page 조회할 페이지 번호
     * @param size 페이지당 상품 수
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param productCategory 상품 카테고리
     * @param productStatus 상품 상태 필터
     * @param request HTTP 요청
     * @return 상품 목록과 페이징 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(
                    name = "category",
                    required = false) String productCategory,
            @RequestParam(
                    name = "status",
                    required = false) Product.ProductStatus productStatus,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        ProductListResponse responseDto = productService.listProducts(
                keyword,
                page,
                size,
                sortBy,
                sortDirection,
                productCategory,
                productStatus);

        // 4. HTTP 응답 생성
        ResponseEntity<ProductListResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 상품 기본 정보를 수정한다.
     *
     * @param requestDto 상품 정보 수정 요청 데이터
     * @param productId 수정할 상품 ID
     * @param request HTTP 요청
     * @return 수정된 상품 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductChangeResponse> changeProduct(
            @RequestBody ProductChangeRequest requestDto,
            @PathVariable("id") Long productId,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        ProductChangeResponse responseDto =
                productService.changeProduct(requestDto, productId);

        // 4. HTTP 응답 생성
        ResponseEntity<ProductChangeResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 상품 상태를 변경한다.
     *
     * @param requestDto 상품 상태 변경 요청 데이터
     * @param productId 상태를 변경할 상품 ID
     * @param request HTTP 요청
     * @return 변경된 상품 상태 정보
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductStatusResponse> statusProduct(
            @RequestBody ProductStatusRequest requestDto,
            @PathVariable("id") Long productId,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        ProductStatusResponse responseDto =
                productService.statusProduct(requestDto, productId);

        // 4. HTTP 응답 생성
        ResponseEntity<ProductStatusResponse> response =
                new ResponseEntity<>(responseDto, HttpStatus.OK);

        // 5. 반환
        return response;
    }

    /**
     * 상품을 삭제한다.
     *
     * @param productId 삭제할 상품 ID
     * @param request HTTP 요청
     * @return 상품 삭제 결과 메시지
     * @throws ResponseStatusException 인증되지 않은 경우
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            @PathVariable("id") Long productId,
            HttpServletRequest request) {

        // 1. 기존 세션 조회
        HttpSession session = request.getSession(false);

        // 2. 관리자 로그인 여부 확인
        if (session == null
                || session.getAttribute("ADMIN_ID") == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized user");
        }

        // 3. 서비스 처리
        productService.deleteProduct(productId);

        // 4. 메시지 전달
        Map<String, String> responseBody =
                Map.of("message", "요청하신 상품이 삭제되었습니다.");

        // 5. HTTP 응답 생성
        ResponseEntity<Map<String, String>> response =
                new ResponseEntity<>(responseBody, HttpStatus.OK);

        // 6. 반환
        return response;
    }
}