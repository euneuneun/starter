package com.starter.product.service;

import com.starter.product.dto.ProductCreateRequestDto;
import com.starter.product.dto.ProductCreateResponseDto;
import com.starter.product.entity.Product;
import com.starter.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    // 속성
    private final ProductRepository productRepository;

    // 생성자
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 기능
    /**
     *  상품등록API
     */
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto productCreateRequestDto) {
        // 1. 데이터 준비
        String name = productCreateRequestDto.getName();
        String category = productCreateRequestDto.getCategory();
        Long price = productCreateRequestDto.getPrice();
        Integer stock = productCreateRequestDto.getStock();
        // 1-1. 검증
        if(stock == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock은 null을 입력할 수 없습니다.");
        }
        Product.ProductStatus status = productCreateRequestDto.getStatus();

        // 2. 엔티티 준비
        Product newProduct = new Product(name, category, price, stock, status);

        // 3. 상품 저장
        Product savedProduct = this.productRepository.save(newProduct);

        // 4. dto 생성해서 entity -> dto 변환
        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(
                savedProduct.getName(), savedProduct.getCategory(), savedProduct.getPrice(),
                savedProduct.getStock(), savedProduct.getStatus());

        // 5. 응답 반환
        return responseDto;
    }
}
