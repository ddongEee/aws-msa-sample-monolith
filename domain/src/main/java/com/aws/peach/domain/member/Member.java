package com.aws.peach.domain.member;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "samplemonolith", name = "members")
@EqualsAndHashCode(of = "memberId")
public class Member {
    @Id
    @Column(name = "memberId")
    private String memberId;

    @Column(name = "memberName")
    private String memberName;
}
