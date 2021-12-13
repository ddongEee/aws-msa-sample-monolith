package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Builder
@Getter
public class OrderNo implements Serializable {

    private String number;

    private OrderNo() {
    }

    public OrderNo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNo orderNo = (OrderNo) o;
        return Objects.equals(number, orderNo.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}