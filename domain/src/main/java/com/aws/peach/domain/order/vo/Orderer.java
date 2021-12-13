package com.aws.peach.domain.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Orderer {
    private String memberId;
    private String name;
}
