package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class OrderLine {
    private static final String PRODUCT_NAME_AND_QUANTITY_FORMAT = " %s(%s)";

    private OrderProduct orderProduct;
    private int quantity;

    public int calculateAmounts() {
        return this.orderProduct.getPrice() * quantity;
    }

    public String getProductNameAndQuantity() {
        return String.format(PRODUCT_NAME_AND_QUANTITY_FORMAT, orderProduct.getProductName(), this.quantity);
    }
}
