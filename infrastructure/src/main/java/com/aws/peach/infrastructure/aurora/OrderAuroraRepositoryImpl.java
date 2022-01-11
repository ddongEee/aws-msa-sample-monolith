package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepositoryCustom;
import com.aws.peach.domain.order.vo.OrderNumber;

import java.util.List;

public class OrderAuroraRepositoryImpl implements OrderRepositoryCustom {

    @Override
    public OrderNumber nextOrderNo() {
        return new OrderNumber("1");
    }
}
