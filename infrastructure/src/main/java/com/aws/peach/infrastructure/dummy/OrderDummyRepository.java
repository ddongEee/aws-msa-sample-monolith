package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNo;
import org.springframework.stereotype.Component;

@Component
public class OrderDummyRepository implements OrderRepository {

    @Override
    public OrderNo nextOrderNo() {
        return new OrderNo("1");
    }

    @Override
    public Order save(Order order) {
        return order;
    }
}
