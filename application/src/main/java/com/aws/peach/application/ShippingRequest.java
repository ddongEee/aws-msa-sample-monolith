package com.aws.peach.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShippingRequest {

    private String country;
    private String city;
    private String zipCode;
    private String telephoneNumber;
    private String receiver;
}