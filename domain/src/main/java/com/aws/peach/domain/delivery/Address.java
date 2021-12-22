package com.aws.peach.domain.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Address {
    private final String name;
    private final String city;
    private final String telephone;
    private final String address1;
    private final String address2;
}
