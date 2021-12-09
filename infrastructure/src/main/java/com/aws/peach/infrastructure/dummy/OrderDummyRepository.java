package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.Order;
import com.aws.peach.domain.order.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderDummyRepository implements OrderRepository {
    @Override
    public Order save(Order order) {
        return order;
    }
}
