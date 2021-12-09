package com.aws.peach.application;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Address {
    private final String zipCode;
    private final String address1;
    private final String address2;

}