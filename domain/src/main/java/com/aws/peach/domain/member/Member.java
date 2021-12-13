package com.aws.peach.domain.member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Member {
    private String memberId;
    private String memberName;
}
