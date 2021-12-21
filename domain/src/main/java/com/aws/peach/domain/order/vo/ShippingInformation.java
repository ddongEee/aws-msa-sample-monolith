package com.aws.peach.domain.order.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShippingInformation {
    private String city;
    private String telephoneNumber;
    private String receiver;
    private String address1;
    private String address2;
}
