
package com.starter.product.entity;

import com.starter.admin.entity.Admin;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.starter.product.enums.ProductStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "name", nullable = false, length = 100)
    private String productName;

    @Column(name = "category", nullable = false, length = 15)
    private String productCategory;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ProductStatus productStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime deletedAt;
    /**
     * JPA에서 사용하는 기본 생성자.
     */
    protected Product() {
    }
    /**
     * 관리자 정보 없이 상품을 생성한다.
     *
     * @param productName 상품명
     * @param productCategory 상품 카테고리
     * @param price 상품 가격
     * @param stock 상품 재고
     * @param productStatus 상품 상태
     */
    public Product(
            String productName,
            String productCategory,
            Long price,
            Integer stock,
            ProductStatus productStatus) {
        this(
                productName,
                productCategory,
                price,
                stock,
                productStatus,
                null);
    }
    /**
     * 관리자 정보를 포함하여 상품을 생성한다.
     *
     * @param productName 상품명
     * @param productCategory 상품 카테고리
     * @param price 상품 가격
     * @param stock 상품 재고
     * @param productStatus 상품 상태
     * @param admin 상품 등록 관리자
     * @throws IllegalArgumentException 상품 정보가 유효하지 않은 경우
     */
    public Product(
            String productName,
            String productCategory,
            Long price,
            Integer stock,
            ProductStatus productStatus,
            Admin admin) {

        if (productName == null || productName.isBlank()
                || productName.length() > 100) {
            throw new IllegalArgumentException(
                    "상품명은 null 이거나 100자를 넘을 수 없습니다.");
        }

        if (productCategory == null || productCategory.isBlank()
                || productCategory.length() > 15) {
            throw new IllegalArgumentException(
                    "카테고리는 null 이거나 15자를 넘을 수 없습니다.");
        }

        if (price == null || price < 0) {
            throw new IllegalArgumentException(
                    "가격은 null 이거나 음수일 수 없습니다.");
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException(
                    "재고는 음수일 수 없습니다.");
        }

        this.productName = productName;
        this.productCategory = productCategory;
        this.price = price;

        if (productStatus == ProductStatus.DISCONTINUED) {
            this.productStatus = ProductStatus.DISCONTINUED;
        } else if (stock == 0) {
            this.productStatus = ProductStatus.OUT_OF_STOCK;
        } else {
            this.productStatus = ProductStatus.ON_SALE;
        }

        this.stock = stock;
        this.admin = admin;
    }
    /**
     * 상품 재고를 변경하고 판매 상태를 갱신한다.
     *
     * @param stock 변경할 재고
     * @throws IllegalArgumentException 재고가 음수인 경우
     */
    public void changeStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException(
                    "재고는 음수일 수 없습니다.");
        }

        this.stock = stock;

        if (productStatus != ProductStatus.DISCONTINUED) {
            if (stock == 0) {
                productStatus = ProductStatus.OUT_OF_STOCK;
            } else {
                productStatus = ProductStatus.ON_SALE;
            }
        }
    }
    /**
     * 상품을 단종 상태로 변경한다.
     */
    public void discontinued() {
        this.productStatus = ProductStatus.DISCONTINUED;
    }
    /**
     * 상품 단종을 해제하고 재고에 따라 판매 상태를 변경한다.
     */
    public void resumeSale() {
        if (stock == 0) {
            productStatus = ProductStatus.OUT_OF_STOCK;
        } else {
            productStatus = ProductStatus.ON_SALE;
        }
    }
    /**
     * 상품 정보를 수정한다.
     *
     * @param productName 변경할 상품명
     * @param productCategory 변경할 카테고리
     * @param price 변경할 가격
     * @throws IllegalArgumentException 상품 정보가 유효하지 않은 경우
     */
    public void changeProduct(
            String productName,
            String productCategory,
            Long price) {

        // 1. 상품명이 전달된 경우에만 검증 및 수정
        if (productName != null) {
            if (productName.isBlank() || productName.length() > 100) {
                throw new IllegalArgumentException(
                        "상품명은 공백이거나 100자를 넘을 수 없습니다.");
            }

            this.productName = productName;
        }

        // 2. 카테고리가 전달된 경우에만 검증 및 수정
        if (productCategory != null) {
            if (productCategory.isBlank() || productCategory.length() > 15) {
                throw new IllegalArgumentException(
                        "카테고리는 공백이거나 15자를 넘을 수 없습니다.");
            }

            this.productCategory = productCategory;
        }

        // 3. 가격이 전달된 경우에만 검증 및 수정
        if (price != null) {
            if (price < 0) {
                throw new IllegalArgumentException(
                        "가격은 음수일 수 없습니다.");
            }

            this.price = price;
        }
    }
    /**
     * 상품을 소프트 삭제 처리한다.
     */
    public void deleteProduct() {
        this.deletedAt = LocalDateTime.now();
    }
    /**
     * 상품 ID를 반환한다.
     *
     * @return 상품 ID
     */
    public Long getProductId() {
        return productId;
    }
    /**
     * 상품명을 반환한다.
     *
     * @return 상품명
     */
    public String getProductName() {
        return productName;
    }
    /**
     * 상품 카테고리를 반환한다.
     *
     * @return 상품 카테고리
     */
    public String getProductCategory() {
        return productCategory;
    }
    /**
     * 상품 가격을 반환한다.
     *
     * @return 상품 가격
     */
    public Long getPrice() {
        return price;
    }
    /**
     * 상품 재고를 반환한다.
     *
     * @return 상품 재고
     */
    public Integer getStock() {
        return stock;
    }
    /**
     * 상품 상태를 반환한다.
     *
     * @return 상품 상태
     */
    public ProductStatus getProductStatus() {
        return productStatus;
    }
    /**
     * 상품 등록 일시를 반환한다.
     *
     * @return 상품 등록 일시
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    /**
     * 상품 최종 수정 일시를 반환한다.
     *
     * @return 최종 수정 일시
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    /**
     * 상품 삭제 일시를 반환한다.
     *
     * @return 상품 삭제 일시
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    /**
     * 상품 등록 관리자를 반환한다.
     *
     * @return 상품 등록 관리자
     */
    public Admin getAdmin() {
        return admin;
    }
}