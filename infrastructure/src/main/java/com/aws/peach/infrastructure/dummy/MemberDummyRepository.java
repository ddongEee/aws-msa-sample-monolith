package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.member.MemberRepository;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNo;
import org.springframework.stereotype.Component;

@Component
public class MemberDummyRepository implements MemberRepository {


    @Override
    public Member findByMemberId(String memberId) {
        return Member.builder().memberId("isheejong").memberName(" 이희종").build();
    }
}
