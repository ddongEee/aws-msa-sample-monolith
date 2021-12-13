package com.aws.peach.domain.order.vo;

public class OrderLine {

    private OrderProduct orderProduct;
    private int quantity;

    private int calculateAmounts() {
        return this.orderProduct.getPrice() * quantity;
    }
}
