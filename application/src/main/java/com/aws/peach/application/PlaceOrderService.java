package com.aws.peach.application;

import com.aws.peach.domain.inventory.InventoryService;
import com.aws.peach.domain.order.Order;
import com.aws.peach.domain.order.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aws.peach.domain.inventory.InventoryService.*;

@Component
public class PlaceOrderService {
//    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
//    private final PaymentRepository paymentService;

    public PlaceOrderService(final InventoryService inventoryService,
                             final OrderRepository orderRepository) {
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
    }

    public String placeOrder(final PlaceOrderRequest request) {
        // 재고 확인
        List<CheckOrderProduct> checkOrderProducts = request.getOrderProducts().stream()
                .map(m -> CheckOrderProduct.of(m.getProductId(), m.getQuantity()))
                .collect(Collectors.toList());

        if (inventoryService.isOutOfStock(checkOrderProducts)) {
            // 종료
            return null;
        }

        // 주문서 생성
        Order order = Order.builder()
                .id("ffdsa123000fdsak")
                .build();

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
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
