package com.aws.peach.domain.order.entity;

import com.aws.peach.domain.order.vo.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Order {

    private OrderNo orderNo;

    private Orderer orderer;

    private List<OrderLine> orderLines;

    private OrderState orderState;

    private Date orderDate;
    private ShippingInformation shippingInformation;
    public String getOrderNo(){
        return this.orderNo.getNumber();
    }
}
