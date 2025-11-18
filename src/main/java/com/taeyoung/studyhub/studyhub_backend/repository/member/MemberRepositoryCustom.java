package com.taeyoung.studyhub.studyhub_backend.repository.member;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findUsernameByEmail(String email);
    Optional<Member> findPasswordByUsername(String username);
}
