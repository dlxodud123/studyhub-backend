package com.taeyoung.studyhub.studyhub_backend.repository.member;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // jwt
    Optional<Member> findByUsername(String username);
}
