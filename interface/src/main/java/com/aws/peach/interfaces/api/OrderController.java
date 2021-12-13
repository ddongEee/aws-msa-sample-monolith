package com.aws.peach.interfaces.api;

import com.aws.peach.application.Address;
import com.aws.peach.application.OrderRequestLine;
import com.aws.peach.application.PlaceOrderService;
import com.aws.peach.application.ShippingRequest;
import org.springframework.http.ResponseEntity;
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

    public OrderController(final PlaceOrderService placeOrderService) {
        this.placeOrderService = placeOrderService;
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
}
