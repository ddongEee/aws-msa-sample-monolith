package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.member.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuroraRepository extends MemberRepository, JpaRepository<Member, String> {
}
