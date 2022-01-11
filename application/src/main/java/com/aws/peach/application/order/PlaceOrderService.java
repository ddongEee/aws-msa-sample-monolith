package com.aws.peach.application.order;

import com.aws.peach.application.inventory.InventoryService;
import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.member.MemberRepository;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.exception.OutOfOrderException;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import com.aws.peach.domain.product.Products;
import com.aws.peach.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aws.peach.application.inventory.InventoryService.CheckOrderProduct;

@Component
public class PlaceOrderService {
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public PlaceOrderService(final InventoryService inventoryService,
                             final OrderRepository orderRepository,
                             final MemberRepository memberRepository,
                             final ProductRepository productRepository
    ) {
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public String placeOrder(final PlaceOrderRequest request) {
        List<CheckOrderProduct> checkOrderProducts = request.getOrderLines().stream()
                .map(m -> CheckOrderProduct.of(m.getProductId(), m.getQuantity()))
                .collect(Collectors.toList());

        // 01. 주문 요청 내용 중 상품의 재고를 확인 한다.
        if (inventoryService.isOutOfStock(checkOrderProducts)) {
            throw new OutOfOrderException();
        }

        // 02. 상품 목록을 조회한다.
        final Products products = Products.create(this.productRepository.findByIdIn(request.getProductIds()));

        // 02. 주문을 번호를 생성한다.
        final OrderNumber orderNumber = this.orderRepository.nextOrderNo();

        // 03. 회원 정보를 조회한다.
        final Member member = this.memberRepository.findByMemberId(request.getOrderer());

        // 04. 제품 주문 정보를 생성한다.
        final List<OrderLine> orderLines = request.getOrderLines().stream()
                .map(m -> OrderLine.builder()
                        .quantity(m.getQuantity())
                        .orderProduct(OrderProduct.builder()
                                .productId(m.getProductId())
                                .productName(products.getProductName(m.getProductId())).build()).build()
                ).collect(Collectors.toList());

        // 05. 주문을 생성한다.
        // 주문은 '결제대기' 상태로 생성되어야 한다.
        final Order order = Order.builder()
                .orderNumber(orderNumber)
                .orderer(Orderer.builder().memberId(member.getMemberId()).name(member.getMemberName()).build())
                .orderLines(orderLines)
                .orderState(OrderState.UNPAID)
                .shippingInformation(makeShippingInformationFrom(request.getShippingRequest()))
                .build();

        // 04. 주문을 저장한다.
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getOrderNumber();
    }

    private ShippingInformation makeShippingInformationFrom(ShippingRequest request) {
        return ShippingInformation.builder()
                .city(request.getCity())
                .telephoneNumber(request.getTelephoneNumber())
                .receiver(request.getReceiver())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static final class PlaceOrderRequest {
        private final String orderer;                       // 주문자 (회원아이디)
        private final List<OrderRequestLine> orderLines;    // 주문상품과 배송지 정보
        private ShippingRequest shippingRequest;

        public List<String> getProductIds() {
            return this.orderLines.stream().map(OrderRequestLine::getProductId).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class OrderRequestLine {
        private String productId;
        private int quantity;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ShippingRequest {
        private String city;
        private String telephoneNumber;
        private String receiver;
        private String address1;
        private String address2;
    }

}
