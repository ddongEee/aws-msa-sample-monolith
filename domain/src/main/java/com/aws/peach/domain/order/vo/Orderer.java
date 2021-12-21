package com.aws.peach.domain.order.vo;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class Orderer {
    private String memberId;
    private String name;
}
