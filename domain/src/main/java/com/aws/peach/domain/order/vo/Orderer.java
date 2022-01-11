package com.aws.peach.domain.order.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class Orderer {
    @Column(name = "memberId")
    private String memberId;
    @Column(name = "memberName")
    private String name;
}
