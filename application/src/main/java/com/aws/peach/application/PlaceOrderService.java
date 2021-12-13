package com.aws.peach.application;

import com.aws.peach.domain.inventory.service.InventoryService;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aws.peach.domain.inventory.service.InventoryService.*;

@Component
public class PlaceOrderService {
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;

    public PlaceOrderService(final InventoryService inventoryService,
                             final OrderRepository orderRepository) {
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
    }

    public String placeOrder(final PlaceOrderRequest request) {

        List<CheckOrderProduct> checkOrderProducts = request.getOrderProducts().stream()
                .map(m -> CheckOrderProduct.of(m.getProductId(), m.getQuantity()))
                .collect(Collectors.toList());

        // 01. 주문 요청 내용 중 상품의 재고를 확인 한다.
        if (inventoryService.isOutOfStock(checkOrderProducts)) {
            // 종료
            return null;
        }

        // 02. 주문을 번호를 생성한다.
        OrderNo orderNo = this.orderRepository.nextOrderNo();

        // 03. 주문을 생성한다.
        Order order = Order.builder()
                .orderNo(orderNo)
                .build();

        // 04. 주문을 저장한다.
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getOrderNo();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static final class PlaceOrderRequest {
        private final List<OrderProduct> orderProducts;
        private final String orderer;
        private final ShippingInfo shippingInfo;
    }
}
