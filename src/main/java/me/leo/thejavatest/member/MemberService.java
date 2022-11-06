package me.leo.thejavatest.member;

import me.leo.thejavatest.domain.Member;
import me.leo.thejavatest.domain.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validatate(Long memberId);

    void notify(Study study);

    void notify(Member member);
}

