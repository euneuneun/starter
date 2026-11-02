
package com.starter.product.service;

import com.starter.admin.entity.Admin;
import com.starter.admin.repository.AdminRepository;
import com.starter.product.dto.request.ProductChangeRequest;
import com.starter.product.dto.request.ProductChangeStockRequest;
import com.starter.product.dto.request.ProductCreateRequest;
import com.starter.product.dto.request.ProductStatusRequest;
import com.starter.product.dto.response.*;
import com.starter.product.entity.Product;
import com.starter.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.starter.product.enums.ProductStatus;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    public ProductService(
            ProductRepository productRepository,
            AdminRepository adminRepository) {

        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }

    // 기능

    /**
     * 상품을 등록한다.
     *
     * @param productCreateRequest 상품 등록 요청 데이터
     * @param adminId 상품을 등록하는 관리자 ID
     * @return 등록된 상품 정보
     */
    @Transactional
    public ProductCreateResponse createProduct(
            ProductCreateRequest productCreateRequest,
            Long adminId) {

        // 1. 데이터 준비
        String productName = productCreateRequest.getProductName();
        String productCategory = productCreateRequest.getProductCategory();
        Long price = productCreateRequest.getPrice();
        Integer stock = productCreateRequest.getStock();

        // 1-1. 검증
        if (stock == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "stock은 null을 입력할 수 없습니다.");
        }

        ProductStatus productStatus =
                productCreateRequest.getProductStatus();

        // 2. 관리자 id로 조회
        Optional<Admin> optionalAdmin =
                adminRepository.findById(adminId);

        // 2-1. 관리자 존재여부 확인
        if (optionalAdmin.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 2-2. 관리자가 있으면
        Admin foundAdmin = optionalAdmin.get();

        // 2. 엔티티 준비
        Product newProduct = new Product(
                productName,
                productCategory,
                price,
                stock,
                productStatus,
                foundAdmin);

        // 3. 상품 저장
        Product savedProduct =
                this.productRepository.save(newProduct);

        // 4. dto 생성해서 entity -> dto 변환
        ProductCreateResponse responseDto = new ProductCreateResponse(
                savedProduct.getProductName(),
                savedProduct.getProductCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getProductStatus());

        // 5. 응답 반환
        return responseDto;
    }

    /**
     * 상품 상세 정보를 조회한다.
     *
     * @param productId 조회할 상품 ID
     * @return 상품 상세 정보
     */
    @Transactional(readOnly = true)
    public ProductDetailResponse detailProduct(Long productId) {

        // 1. id로 상품 조회
        Optional<Product> optionalProduct =
                this.productRepository.findById(productId);

        // 2. 상품 존재 여부 확인, 없으면 예외처리
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Product foundProduct = optionalProduct.get();

        // 3-1. 삭제된 상품인지 확인, 삭제된 상품이면 예외처리
        if (foundProduct.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 4. entity -> dto 변환
        ProductDetailResponse responseDto =
                new ProductDetailResponse(foundProduct);

        // 5. 응답 반환
        return responseDto;
    }

    /**
     * 상품 재고를 변경한다.
     *
     * @param requestDto 상품 재고 변경 요청 데이터
     * @param productId 변경할 상품 ID
     * @return 변경된 상품 재고 정보
     */
    @Transactional
    public ProductChangeStockResponse changeStock(
            ProductChangeStockRequest requestDto,
            Long productId) {

        // 1. id로 상품 조회
        Optional<Product> optionalProduct =
                this.productRepository.findById(productId);

        // 2. 상품 존재 여부 확인, 없으면 예외처리
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Product foundProduct = optionalProduct.get();

        // 3-1. 삭제된 상품인지 확인, 삭제된 상품이면 예외처리
        if (foundProduct.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 4. RequestDto 에서 변경할 재고 가져오기
        Integer stock = requestDto.getStock();

        // 4-1. 가져온 재고 검증
        if (stock == null || stock < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 4-2. 재고 변경
        foundProduct.changeStock(stock);

        // 4-3. 변경된 재고 db 동기화
        productRepository.flush();

        // 5. entity -> dto 변환
        ProductChangeStockResponse responseDto =
                new ProductChangeStockResponse(
                        foundProduct.getProductId(),
                        foundProduct.getStock(),
                        foundProduct.getProductStatus(),
                        foundProduct.getUpdatedAt());

        // 6. 응답 반환
        return responseDto;
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
     * @return 상품 목록과 페이징 정보
     */
    @Transactional(readOnly = true)
    public ProductListResponse listProducts(
            String keyword,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String productCategory,
            ProductStatus productStatus) {

        // 1. 검증

        // 1-1. page 검증
        if (page == null || page < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 1-2. size 검증
        if (size == null || size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 1-3. sortDirection 검증
        if (sortDirection == null ||
                (!sortDirection.equals("asc")
                        && !sortDirection.equals("desc"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 1-4. sortBy 검증
        if (sortBy == null ||
                (!sortBy.equals("createdAt")
                        && !sortBy.equals("price")
                        && !sortBy.equals("stock"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 1-5. keyword 검증
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        // 1-6. category 검증
        if (productCategory != null && productCategory.isBlank()) {
            productCategory = null;
        }

        // 1-7. status는 컨트롤러에서 enum으로 변환 -> 검증생략

        // 2. 정렬 방향 변환
        Sort.Direction direction =
                Sort.Direction.fromString(sortDirection); // "asc" / "desc" 문자열을 변환

        // 3. 정렬 조건 만들기
        Sort sort = Sort.by(direction, sortBy);

        // 4. 페이징 조건 만들기
        Pageable pageable =
                PageRequest.of(page - 1, size, sort);

        // 5. 검색 조건에 따라 상품 목록 조회
        Page<Product> pageProduct =
                productRepository.searchProducts(
                        keyword,
                        productCategory,
                        productStatus,
                        pageable);

        // 6. entity -> dto 목록 변환
        List<ProductDetailResponse> productDtoList =
                pageProduct.getContent().stream()
                        .map(product -> new ProductDetailResponse(product))
                        .toList();

        // 7. 응답 dto
        ProductListResponse responseDto = new ProductListResponse(
                productDtoList,
                page,
                size,
                pageProduct.getTotalElements(),
                pageProduct.getTotalPages());

        // 8. 응답 반환
        return responseDto;
    }

    /**
     * 상품 기본 정보를 수정한다.
     *
     * @param requestDto 상품 정보 수정 요청 데이터
     * @param productId 수정할 상품 ID
     * @return 수정된 상품 정보
     */
    @Transactional
    public ProductChangeResponse changeProduct(
            ProductChangeRequest requestDto,
            Long productId) {

        // 1. id로 상품 조회
        Optional<Product> optionalProduct =
                this.productRepository.findById(productId);

        // 2. 상품 존재 여부 확인, 없으면 예외처리
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Product foundProduct = optionalProduct.get();

        // 3-1. 삭제된 상품인지 확인, 삭제된 상품이면 예외처리
        if (foundProduct.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 4. RequestDto에서 변경할 정보 가져오기
        String productName = requestDto.getProductName();
        String productCategory = requestDto.getProductCategory();
        Long price = requestDto.getPrice();

        // 4-1. 가져온 정보 검증
        foundProduct.changeProduct(
                productName,
                productCategory,
                price);

        // 4-2. 변경된 정보 db 동기화
        productRepository.flush();

        // 5. entity -> dto 변환
        ProductChangeResponse responseDto = new ProductChangeResponse(
                foundProduct.getProductId(),
                foundProduct.getProductName(),
                foundProduct.getProductCategory(),
                foundProduct.getPrice(),
                foundProduct.getUpdatedAt());

        // 6. 응답 반환
        return responseDto;
    }

    /**
     * 상품 상태를 변경한다.
     *
     * @param requestDto 상품 상태 변경 요청 데이터
     * @param productId 상태를 변경할 상품 ID
     * @return 변경된 상품 상태 정보
     */
    @Transactional
    public ProductStatusResponse statusProduct(
            ProductStatusRequest requestDto,
            Long productId) {

        // 1. id로 상품 조회
        Optional<Product> optionalProduct =
                this.productRepository.findById(productId);

        // 2. 상품 존재 여부 확인, 없으면 예외처리
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Product foundProduct = optionalProduct.get();

        // 3-1. 삭제된 상품인지 확인, 삭제된 상품이면 예외처리
        if (foundProduct.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 4. RequestDto에서 변경할 정보 가져오기
        Boolean discontinued = requestDto.getDiscontinued();

        // 4-1. 가져온 정보 검증
        if (discontinued == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 요청입니다.");
        }

        // 5. 단종 여부에 따라 상품 상태 변경
        if (discontinued == true) {
            foundProduct.discontinued();
        } else {
            foundProduct.resumeSale();
        }

        // 5-1. 변경된 정보 db 동기화
        productRepository.flush();

        // 6. entity -> dto 변환
        ProductStatusResponse responseDto = new ProductStatusResponse(
                foundProduct.getProductId(),
                foundProduct.getProductStatus(),
                foundProduct.getUpdatedAt());

        // 7. 응답 반환
        return responseDto;
    }

    /**
     * 상품을 논리 삭제한다.
     *
     * @param productId 삭제할 상품 ID
     */
    @Transactional
    public void deleteProduct(Long productId) {

        // 1. id로 상품 조회
        Optional<Product> optionalProduct =
                this.productRepository.findById(productId);

        // 2. 상품 존재 여부 확인, 없으면 예외처리
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 3. 객체 꺼내기
        Product foundProduct = optionalProduct.get();

        // 3-1. 삭제된 상품인지 확인, 삭제된 상품이면 예외처리
        if (foundProduct.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 데이터를 찾을 수 없습니다.");
        }

        // 4. 논리 삭제 처리
        foundProduct.deleteProduct();
    }
}