package com.taeyoung.studyhub.studyhub_backend.repository.member;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;

public interface MemberRepositoryCustom {
    Member findUsernameByEmail(String email);
}
