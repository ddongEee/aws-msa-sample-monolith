package com.aws.peach.domain.member;


public interface MemberRepository {
    Member findByMemberId(final String memberId);
}
