package com.aws.peach.domain.order.repository;

import com.aws.peach.domain.order.vo.OrderNumber;

public interface OrderRepositoryCustom {
    OrderNumber nextOrderNo();
}
