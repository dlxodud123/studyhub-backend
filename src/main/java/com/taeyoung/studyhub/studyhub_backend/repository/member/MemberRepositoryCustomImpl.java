package com.taeyoung.studyhub.studyhub_backend.repository.member;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

//    private final EntityManager em;
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//        List<Member> list = em.createQuery("select m from Member m where m.email = :email")
//                .setParameter("email", email)
//                .getResultList();
//
//        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
//    }
//
//    @Override
//    public Optional<Member> findByUsername(String username) {
//        List<Member> list = em.createQuery("select m from Member m where m.username = :username")
//                .setParameter("username", username)
//                .getResultList();
//
//        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
//    }
}
