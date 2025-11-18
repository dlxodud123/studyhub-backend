package com.taeyoung.studyhub.studyhub_backend.repository.member;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public Member findUsernameByEmail(String email) {
        List<Member> list = em.createQuery("select m from Member m where m.email = :email")
                .setParameter("email", email)
                .getResultList();

        return list.get(0);
    }
}
