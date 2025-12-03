package com.taeyoung.studyhub.studyhub_backend.repository.admin;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminDashboardResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminMembersResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.admin.response.AdminStudiesResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')",
                    member.createdAt
                )
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
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')",
                        study.createdAt
                    )
            ))
            .from(study)
            .leftJoin(study.category, category)
            .orderBy(study.createdAt.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public Page<AdminMembersResponseDto> findMembers(Pageable pageable) {
        // content 조회
        List<AdminMembersResponseDto> content = queryFactory
            .select(Projections.constructor(
                AdminMembersResponseDto.class,
                member.id,
                member.username,
                member.email,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')",
                    member.createdAt
                ),
                member.role
            ))
            .from(member)
            .orderBy(member.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // total count 조회
        Long total = queryFactory
            .select(member.count())
            .from(member)
            .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<AdminStudiesResponseDto> findStudies(Pageable pageable) {
        // content 조회
        List<AdminStudiesResponseDto> content = queryFactory
                .select(Projections.constructor(
                        AdminStudiesResponseDto.class,
                        study.id,
                        study.title,
                        study.category.name,
                        Expressions.stringTemplate(
                                "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')",
                                study.createdAt
                        )
                ))
                .from(study)
                .orderBy(study.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count 조회
        Long total = queryFactory
                .select(study.count())
                .from(study)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
