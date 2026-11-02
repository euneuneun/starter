package com.starter.order.service;

import com.starter.admin.entity.Admin;
import com.starter.admin.enums.AdminRole;
import com.starter.admin.repository.AdminRepository;
import com.starter.customer.entity.Customer;
import com.starter.customer.repository.CustomerRepository;
import com.starter.order.dto.request.OrderCancelRequest;
import com.starter.order.dto.request.OrderCreateRequest;
import com.starter.order.dto.response.OrderDetailResponse;
import com.starter.order.dto.response.OrderSummaryResponse;
import com.starter.order.entity.Order;
import com.starter.order.entity.OrderItem;
import com.starter.order.enums.OrderStatus;
import com.starter.order.repository.OrderItemRepository;
import com.starter.order.repository.OrderRepository;
import com.starter.product.entity.Product;
import com.starter.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {

    // 속성
    private static final int ORDER_PAGE_SIZE = 10; // 주문 목록 한 페이지 크기

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 생성자
    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            AdminRepository adminRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }

    // 기능

    /**
     * CS 관리자가 고객 대신 주문을 생성한다.
     * <p>다음 경우에는 주문을 생성하지 않는다.</p>
     * <ul>
     *     <li>관리자 ID, 고객 ID, 주문 ID가 존재하지 않는 경우</li>
     *     <li>상품 상태가 단종/품절인 경우</li>
     *     <li>주문수량이 재고수량보다 많은 경우</li>
     * </ul>
     * <p>주문이 생성되면 다음과 같은 일이 진행된다.</p>
     * <ul>
     *     <li>주문 수량만큼 상품 재고가 차감된다.</li>
     *     <li>재고 변경에 따라 상품 상태가 자동 전환 처리된다.</li>
     *     <li>주문 번호가 자동 생성되어 부여된다. (ex: 20260901-003)</li>
     * </ul>
     *
     * @param adminId 주문을 등록한 CS 관리자 ID
     * @param request 주문 고객 ID, 주문 상품 ID, 주문 수량
     */
    @Transactional
    public void createOrder(Long adminId, OrderCreateRequest request) {

        // 1. admin, customer, product 객체 꺼내기
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다."));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 고객입니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."));

        // 2. 검증
        // 2-1. 주문한 상품 상태 확인
        if (product.getStatus() == Product.ProductStatus.DISCONTINUED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "현재 상품이 단종되어 주문할 수 없습니다.");
        } else if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "현재 상품이 품절되어 주문할 수 없습니다.");
        }

        // 2-2. 주문 수량만큼 상품 재고 검증 처리
        if (product.getStock() < request.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "주문 수량이 재고 수량보다 많습니다.");
        }

        // 3. 재고 차감
        // 3-1. 재고 계산
        Integer stock = product.getStock() - request.getQuantity();
        // 3-2. 재고 저장 ( 품절 처리 자동 변환)
        product.changeStock(stock);

        // 4. 엔티티 생성
        // 4-1. 엔티티에 넣을 값 계산
        // 4-1-1.( 총 금액 = 상품 단가 * 수량)
        Long totalPrice = product.getPrice() * request.getQuantity();
        // 4-1-2. 주문 번호 생성
        // TODO: 메서드로 빼기
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);                 // 20261029
        long todayCount = orderRepository.countByOrderNumberStartingWith(today + "-") + 1; // 3
        String orderNumber = String.format("%s-%03d", today, todayCount);                        // 20261029-003

        // 4-2. Order 엔티티 생성
        Order order = new Order(customer, admin, orderNumber, totalPrice);
        // 4-3 OrderItem 엔티티 생성
        OrderItem orderItem = new OrderItem(
                order,
                product,
                product.getName(),  // 주문한 순간의 상품 이름
                product.getPrice(), // 주문한 순간의 상품 가격
                request.getQuantity()
        );

        // 5. 저장
        // 5-1. 주문 저장
        orderRepository.save(order);
        // 5-2. 주문 상품 저장
        orderItemRepository.save(orderItem);

    }

    /**
     * 주문 상세 정보를 조회한다.
     *
     * @param orderId 조회할 주문 ID
     * @return 주문 상세 정보
     */
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId) {

        // 1. 엔티티 준비
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        OrderItem orderItem = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문 상품입니다."));

        // 2. 등록 관리자 정보 (관리자 없이 생성된 주문은 null)
        Admin registerAdmin = order.getRegisterAdmin();
        String adminEmail = (registerAdmin != null) ? registerAdmin.getEmail() : null;
        AdminRole adminRole = (registerAdmin != null) ? registerAdmin.getRole() : null;

        // 3. 응답 생성
        OrderDetailResponse response = new OrderDetailResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                adminEmail,
                adminRole
        );

        return response;
    }

    /**
     * 주문 목록을 조회한다.
     *
     * @param keyword   검색어 (주문번호, 고객명)
     * @param status    주문 상태 필터 (준비중, 배송중, 배송완료, 취소됨)/ null이면 전체 상태
     * @param sortBy    정렬 기준값 (주문번호, 고객명, 상품명, 수량, 금액, 주문일, 상태)
     * @param direction ASC, DESC
     * @param page      조회할 페이지 번호
     * @return 페이징된 주문 목록
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getOrderList(
            String keyword,
            OrderStatus status,
            String sortBy,
            Sort.Direction direction,
            int page) {

        // 1. 정렬 및 페이징 처리
        String sortProperty = switch (sortBy) {
            case "orderNumber" -> "order.orderNumber";
            case "customerName" -> "order.customer.name";
            case "productName" -> "productName";
            case "quantity" -> "quantity";
            case "totalPrice" -> "order.totalPrice";
            case "createdAt" -> "order.createdAt";
            case "status" -> "order.status";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "정렬할 수 없는 항목입니다.");
        };

        Sort sort = Sort.by(direction, sortProperty);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), ORDER_PAGE_SIZE, sort);

        // 2. 조회
        // 2-1. 빈 검색어("")는 null로 바꿔서 검색어 조건 생략
        String searchKeyword = StringUtils.hasText(keyword) ? keyword : null;
        // 2-2. 검색어, 상태 조건으로 조회 (null인 조건은 쿼리에서 생략)
        Page<OrderItem> orderItems = orderItemRepository.searchOrders(searchKeyword, status, pageable);

        // 3. Page<entity> -> Page<dto>

        // 3-1. page안에 있는 entity 하나씩 뽑아서 dto로 변환
        List<OrderSummaryResponse> orderSummaryList = new ArrayList<>();

        // orderItems (Page<OrderItem>)
        // ├─ 데이터 목록       → getContent()        = [주문상품1, 주문상품2, ..., 주문상품10]
        // ├─ 현재 페이지 번호   → getNumber()         = 1
        // ├─ 페이지 크기       → getSize()           = 10
        // ├─ 전체 데이터 개수   → getTotalElements()  = 37
        // └─ 전체 페이지 수    → getTotalPages()      = 4
        for (OrderItem orderItem : orderItems.getContent()) {

            Order order = orderItem.getOrder();

            OrderSummaryResponse orderSummaryResponse = new OrderSummaryResponse(
                    order.getOrderId(),
                    order.getOrderNumber(),
                    order.getCustomer().getName(),
                    orderItem.getProductName(),
                    orderItem.getQuantity(),
                    order.getTotalPrice(),
                    order.getCreatedAt(),
                    order.getStatus(),
                    order.getRegisterAdmin() == null ? null :order.getRegisterAdmin().getName()
            );

            // 3-2. dto -> list <dto>
            orderSummaryList.add(orderSummaryResponse);
        }

        // 3-3. list<dto> -> page<dto>
        Page<OrderSummaryResponse> response = new PageImpl<>(
                orderSummaryList,
                orderItems.getPageable(),
                orderItems.getTotalElements()
        );

        return response;
    }

    /**
     * 주문 상태를 다음 단계로 바꾼다.
     *
     * @param orderId 상태 수정할 주문 ID
     */
    @Transactional
    public void updateOrderStatus(Long orderId) {

        // 1. orderId에 해당하는 엔티티 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        order.updateOrderStatus();
    }

    /**
     * 주문을 취소한다.
     *
     * @param orderId 취소할 주문 ID
     * @param request 주문 취소 요청 (취소 사유)
     */
    @Transactional
    public void cancelOrder(Long orderId, OrderCancelRequest request) {

        // 1. orderId에 해당하는 엔티티 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        order.cancelOrder(request.getCancelReason());
    }
}
