package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class OrderLine {

    private OrderProduct orderProduct;
    private int quantity;

    private int calculateAmounts() {
        return this.orderProduct.getPrice() * quantity;
    }
}
