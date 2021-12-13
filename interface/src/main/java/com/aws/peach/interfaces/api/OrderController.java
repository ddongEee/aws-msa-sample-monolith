package com.aws.peach.interfaces.api;

import com.aws.peach.application.OrderViewService;
import com.aws.peach.application.PlaceOrderService;
import com.aws.peach.domain.order.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.aws.peach.application.PlaceOrderService.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final PlaceOrderService placeOrderService;
    private final OrderViewService orderViewService;

    public OrderController(final PlaceOrderService placeOrderService,
                           final OrderViewService orderViewService
    ) {
        this.placeOrderService = placeOrderService;
        this.orderViewService = orderViewService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder() {
        List<OrderRequestLine> orderProducts = new ArrayList<>();

        orderProducts.add(OrderRequestLine.of("01", 5));

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .orderLines(orderProducts)
                .orderer("HeeJong")
                .shippingRequest(ShippingRequest.builder()
                        .country("대한민국")
                        .receiver("김학성")
                        .city("서울")
                        .zipCode("12345")
                        .telephoneNumber("01012341234")
                        .build())
                .build();
        String orderId = placeOrderService.placeOrder(request);

        return ResponseEntity.ok(orderId);
    }

    @GetMapping
    public ResponseEntity<Order> getOrder(){
        Order order = this.orderViewService.getOrder("1");
        return ResponseEntity.ok(order);
    }
}
