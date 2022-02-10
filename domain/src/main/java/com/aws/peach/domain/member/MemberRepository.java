package com.aws.peach.domain.member;

public interface MemberRepository extends MemberRepositoryCustom {
    Member findByMemberId(String orderer);
    Member save(final Member member);
}
