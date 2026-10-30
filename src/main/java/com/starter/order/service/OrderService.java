package com.starter.order.service;

import com.starter.admin.entity.Admin;
import com.starter.admin.repository.AdminRepository;
import com.starter.customer.entity.Customer;
import com.starter.customer.repository.CustomerRepository;
import com.starter.order.dto.response.OrderDetailResponse;
import com.starter.order.dto.response.OrderSummaryResponse;
import com.starter.order.entity.Order;
import com.starter.order.entity.OrderItem;
import com.starter.order.enums.OrderStatus;
import com.starter.order.repository.OrderItemRepository;
import com.starter.order.repository.OrderRepository;
import com.starter.product.entity.Product;
import com.starter.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderService {

    // 속성
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 생성자

    // 기능

    /** CS 주문을 생성한다.
     *
     * <ul>
     * <li>주문 수량만큼 상품 재고 검증 및 차감 처리</li>
     * <li>재고 변경에 따른 상품 상태 자동 전환 처리</li>
     * <li>상품이 단종/품절 상태인 경우 주문 생성 불가</li>
     * <li>주문 번호 자동 생성 (ex: 20260901-003)</li>
     * </ul>
     * @param adminId 주문한 CS ID
     * @param customerId 주문한 고객 ID
     * @param productId 주문한 상품 ID
     * @param quantity 주문 수량
     * @return
     */
    // TODO: 예외 ResponseStatusException으로 교체
    @Transactional
    public void createOrder(Long adminId, Long customerId, Long productId, Integer quantity){

        // 1.admmin, customer, product 객체 꺼내기
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(()-> new IllegalStateException("존재하지 않는 관리자입니다."));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new IllegalStateException("존재하지 않는 고객입니다."));

        Product product =productRepository.findById(productId)
                .orElseThrow(()-> new IllegalStateException("존재하지 않는 상품입니다."));

        // 2. 검증
        // 2-1. 주문한 상품 상태 확인
        if(product.getStatus() == Product.ProductStatus.DISCONTINUED)
            throw new IllegalStateException("현재 상품이 단종되어 주문할 수 없습니다.");
        else if(product.getStatus() == Product.ProductStatus.OUT_OF_STOCK)
            throw new IllegalStateException("현재 상품이 품절되어 주문할 수 없습니다.");

        // 2-2. 주문 수량만큼 상품 재고 검증 처리
        if(product.getStock() < quantity)
            throw new IllegalStateException("주문 수량이 재고 수량보다 많습니다.");


        // 3. 재고 차감
        // 3-1. 재고 계산
        Integer stock = product.getStock() - quantity;
        // 3-2. 재고 변경에 따른 상품 상태 자동 전환 처리
        if(stock==0){
            // TODO: 상품 품절 메서드 생성 후 교체 예정
            product.discontinued();
        }
        // 3-3. 재고 저장
        product.changeStock(stock);

        // 4. 엔티티 생성
        // 4-1. 엔티티에 넣을 값 계산
        // 4-1-1.( 총 금액 = 상품 단가 * 수량)
        Long totalPrice = product.getPrice() * quantity;
        // 4-1-2. 주문 번호 생성
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20261029
        long todayCount = orderRepository.countByOrderNumberStartingWith(today + "-") + 1; // 20261029-003
        String orderNumber = String.format("%s-%03d", today, todayCount);

        // 4-2. Order 엔티티 생성
        Order order = new Order( customer, admin, orderNumber, totalPrice);
        // 4-3 OrderItem 엔티티 생성
        OrderItem orderItem = new OrderItem(
                order,
                product,
                product.getName(), // 주문한 순간의 상품 이름
                product.getPrice(), // 주문한 순간의 상품 가격
                quantity
        );

        // 5. 저장
        // 5-1. 주문 저장
        Order saveOrder = orderRepository.save(order);
        // 5-2. 주문 상품 저장
        OrderItem saveOrderItem = orderItemRepository.save(orderItem);

    }

    /**
     *  주문 상세 조회
     *
     * @param orderId 조회할 주문 ID
     * @return 주문 상세 정보
     */
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId){

        // 1. 엔티티 준비
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        OrderItem orderItem = orderItemRepository.findById(orderId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문 상품입니다."));

        OrderDetailResponse response = new OrderDetailResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getRegisterAdmin().getEmail(),
                order.getRegisterAdmin().getRole()
        );

        return response;
    }

    /**
     * 주문 목록 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getOrderList(
            String keyword,
            OrderStatus status,
            String sortBy,
            Sort.Direction direction,
            int page,
            int size){

        // 1. 정렬 및 페이징 처리
        String sortProperty = switch(sortBy){
            case "orderNumber" -> "order.orderNumber";
            case "customerName" -> "order.customer.name";
            case "produdctName" -> "productName";
            case "quantity" -> "quantity";
            case "totalPrice" -> "order.totalPrice";
            case "createdAt" -> "order.createdAt";
            case "status" -> "order.status";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "정렬할 수 없는 항목입니다.");
        };

        Sort sort = Sort.by(direction, sortProperty);
        Pageable pageable = PageRequest.of(Math.max(page-1, 0), size, sort);

        // 2. 경우에 따른 조회
        Page<OrderItem> orderItems;
        // 2-1. keyword O, status O
        if(StringUtils.hasText(keyword) && status != null)
            orderItems = orderItemRepository.findByOrder_OrderNumberContainingAndOrder_StatusOrOrder_Customer_NameContainingAndOrder_Status(
                    keyword, status, keyword, status, pageable);
        else if (StringUtils.hasText(keyword) || status != null){
            // 2-2. keyword O, status X
            if(StringUtils.hasText(keyword))
                orderItems = orderItemRepository.findByOrder_OrderNumberContainingOrOrder_Customer_NameContaining(
                        keyword, keyword, pageable);
            // 2-3 keyword X, status O
            else
                orderItems = orderItemRepository.findByOrder_Status(status, pageable);
        }
        else{
            orderItems = orderItemRepository.findAll(pageable);
        }

        // 3. Page<entity> -> Page<dto>

        // 3-1. page안에 있는 entity 하나씩 뽑아서 dto로 변환
        List<OrderSummaryResponse> orderSummaryList = new ArrayList<>();

        // orderItems (Page<OrderItem>)
        // ├─ 데이터 목록       → getContent()        = [주문상품1, 주문상품2, ..., 주문상품10]
        // ├─ 현재 페이지 번호  → getNumber()         = 1
        // ├─ 페이지 크기       → getSize()           = 10
        // ├─ 전체 데이터 개수  → getTotalElements()  = 37
        // └─ 전체 페이지 수    → getTotalPages()     = 4
        for(OrderItem orderItem :  orderItems.getContent()){

            Order order = orderItem.getOrder();

            // TODO: total_price OrderItem으로 변경?
            OrderSummaryResponse orderSummaryResponse = new OrderSummaryResponse(
                    order.getOrderId(),
                    order.getOrderNumber(),
                    order.getCustomer().getName(),
                    orderItem.getProductName(),
                    orderItem.getQuantity(),
                    order.getTotalPrice(),
                    order.getCreatedAt(),
                    order.getStatus(),
                    order.getRegisterAdmin().getName()
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
     * 주문 상태 수정 (준비중 -> 배송중 -> 배송완료)
     *
     * @param orderId
     */
    @Transactional
    public void updateOrderStatus(Long orderId){

        // 1. orderId에 해당하는 엔티티 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        order.updateOrderStatus();
    }

}
