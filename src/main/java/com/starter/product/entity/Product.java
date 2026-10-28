package com.starter.product.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    // Enum 정의
    // 상품 상태
    public enum ProductStatus {
        ON_SALE, OUT_OF_STOCK, DISCONTINUED
    }

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // private Long adminId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15)
    private String category;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ProductStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    // 생성자
    // JPA 기본 생성자
    protected Product() {
    }

    public Product(String name, String category, Long price, int stock, boolean discontinued) {
        // 잘못입력한 데이터 차단
        if(name == null || name.isBlank() || name.length() > 100) {
            throw new IllegalArgumentException("상품명은 null 이거나 100자를 넘을 수 없습니다.");
        }
        if(category == null || category.isBlank() || category.length() > 15) {
            throw new IllegalArgumentException("카테고리는 null 이거나 15자를 넘을 수 없습니다.");
        }
        if(price == null || price < 0) {
            throw new IllegalArgumentException("가격은 null 이거나 음수일 수 없습니다.");
        }
        if(stock < 0) {
            throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
        }
        this.name = name;
        this.category = category;
        this.price = price;
        if(discontinued == true) {
            status = ProductStatus.DISCONTINUED;
        } else {
            if (stock == 0) {
                status = ProductStatus.OUT_OF_STOCK;
            } else {
                status = ProductStatus.ON_SALE;
            }
        }
        this.stock = stock;
    }
    // 기능
    // 재고 변경하기
    public void changeStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
        }
        this.stock = stock;
        if(this.status != ProductStatus.DISCONTINUED) {
            if(stock == 0) {
                status = ProductStatus.OUT_OF_STOCK;
            } else {
                status = ProductStatus.ON_SALE;
            }
        }
    }
    // 상품 단종 설정
    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }
    // 상품 단종 해제
    public void resumeSale() {
        if(stock == 0) {
            status = ProductStatus.OUT_OF_STOCK;
        } else {
            status = ProductStatus.ON_SALE;
        }
    }
    // 상품 정보 수정
    public void changeProduct(String name, String category, Long price) {
        if(name == null || name.isBlank() || name.length() > 100) {
            throw new IllegalArgumentException("상품명은 null 이거나 100자를 넘을 수 없습니다.");
        }
        if(category == null || category.isBlank() || category.length() > 15) {
            throw new IllegalArgumentException("카테고리는 null 이거나 15자를 넘을 수 없습니다.");
        }
        if(price == null || price < 0) {
            throw new IllegalArgumentException("가격은 null 이거나 음수일 수 없습니다.");
        }
        this.name = name;
        this.category = category;
        this.price = price;
    }
    // 상품 삭제 처리
    public void deleteProduct() {
        this.deletedAt = LocalDateTime.now();
    }
}
