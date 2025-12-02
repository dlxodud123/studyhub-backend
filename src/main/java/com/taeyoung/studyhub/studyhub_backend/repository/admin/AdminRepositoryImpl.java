package com.taeyoung.studyhub.studyhub_backend.repository.admin;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.taeyoung.studyhub.studyhub_backend.domain.member.QMember.*;
import static com.taeyoung.studyhub.studyhub_backend.domain.study.QCategory.*;
import static com.taeyoung.studyhub.studyhub_backend.domain.study.QStudy.*;

@Repository
public class AdminRepositoryImpl implements AdminRepository{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AdminDashboardResponseDto.RecentMember> findRecentMembers() {
        return queryFactory
            .select(Projections.constructor(
                AdminDashboardResponseDto.RecentMember.class,
                member.id,
                member.username,
                member.email,
                member.createdAt.stringValue()
            ))
            .from(member)
            .orderBy(member.createdAt.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public List<AdminDashboardResponseDto.RecentStudy> findRecentStudies() {
        return queryFactory
            .select(Projections.constructor(
                    AdminDashboardResponseDto.RecentStudy.class,
                    study.id,
                    study.title,
                    study.category.name,
                    study.createdAt.stringValue()
            ))
            .from(study)
            .leftJoin(study.category, category)
            .orderBy(study.createdAt.desc())
            .limit(3)
            .fetch();
    }
}
