package com.aws.peach.application;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ShippingInfo {
    private Address address;
    private String receiver;
}