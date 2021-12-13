package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderLine {

    private OrderProduct orderProduct;
    private int quantity;

    private int calculateAmounts() {
        return this.orderProduct.getPrice() * quantity;
    }
}
