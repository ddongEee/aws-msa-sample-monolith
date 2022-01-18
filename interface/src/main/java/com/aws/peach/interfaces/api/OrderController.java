package com.aws.peach.interfaces.api;

import com.aws.peach.application.order.OrderViewService;
import com.aws.peach.application.order.PlaceOrderService;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.aws.peach.application.order.PlaceOrderService.*;

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
    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderRequest request) {
        String orderId = placeOrderService.placeOrder(request);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId){
        Optional<Order> order = this.orderViewService.getOrder(new OrderNumber(orderId));
        return ResponseEntity.of(order);
    }
}
