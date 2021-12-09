package com.aws.peach.interfaces.api;

import com.aws.peach.application.Address;
import com.aws.peach.application.OrderProduct;
import com.aws.peach.application.PlaceOrderService;
import com.aws.peach.application.ShippingInfo;
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
        List<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(OrderProduct.of("yellowPeach", 2));
        orderProducts.add(OrderProduct.of("whitePeach", 1));

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .orderProducts(orderProducts)
                .orderer("HeeJong")
                .shippingInfo(ShippingInfo.builder()
                        .address(Address.builder()
                                .zipCode("12300")
                                .address1("Seoul")
                                .address2("Songpa")
                                .build())
                        .receiver("Haksung")
                        .build())
                .build();
        String orderId = placeOrderService.placeOrder(request);
        return ResponseEntity.ok(orderId);
    }
}
