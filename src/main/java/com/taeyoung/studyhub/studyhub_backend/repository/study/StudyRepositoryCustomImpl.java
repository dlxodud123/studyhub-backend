package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.taeyoung.studyhub.studyhub_backend.domain.member.QMember;
import com.taeyoung.studyhub.studyhub_backend.domain.study.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class StudyRepositoryCustomImpl implements StudyRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public StudyRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Study> searchStudies(String searchType, String keyword, Long categoryId, Pageable pageable) {

        QStudy study = QStudy.study;
        QMember member = QMember.member;
        QStudyTag studyTag = QStudyTag.studyTag;
        QTag tag = QTag.tag;

        // 실제 content 조회
        List<Study> content = queryFactory
            .select(study).distinct()
            .from(study)
            .leftJoin(study.member, member).fetchJoin()
            .leftJoin(study.studyTags, studyTag)
            .leftJoin(studyTag.tag, tag)
            .where(
                titleContains(searchType, keyword),
                contentContains(searchType, keyword),
                writerContains(searchType, keyword),
                categoryEq(categoryId)
            )
            .orderBy(study.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // countQuery
        JPAQuery<Long> countQuery = queryFactory
            .select(study.countDistinct())
            .from(study)
            .leftJoin(study.member, member)
            .leftJoin(study.studyTags, studyTag)
            .leftJoin(studyTag.tag, tag)
            .where(
                titleContains(searchType, keyword),
                contentContains(searchType, keyword),
                writerContains(searchType, keyword),
                categoryEq(categoryId)
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Study> searchStudiesByTags(List<String> tags, Long categoryId, Pageable pageable) {

        // 태그 조건 없이 전체 스터디 조회
        if (tags == null || tags.isEmpty()) {
            return searchStudies(null, null, categoryId, pageable);
        }

        QStudy study = QStudy.study;
        QMember member = QMember.member;
        QStudyTag stSub = new QStudyTag("stSub");
        QTag tSub = new QTag("tSub");
        
        // 실제 content 조회
        JPAQuery<Study> query = queryFactory
            .select(study).distinct()
            .from(study)
            .leftJoin(study.member, member).fetchJoin()
            .where(categoryEq(categoryId))
            .where(
                JPAExpressions
                    .select(stSub.study.id)
                    .from(stSub)
                    .leftJoin(stSub.tag, tSub)
                    .where(stSub.study.eq(study).and(tSub.name.in(tags)))
                    .groupBy(stSub.study.id)
                    .having(stSub.id.countDistinct().eq((long) tags.size()))
                    .exists()
            )
            .orderBy(study.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<Study> content = query.fetch();

        // countQuery
        JPAQuery<Long> countQuery = queryFactory
            .select(study.countDistinct())
            .from(study)
            .where(categoryEq(categoryId))
            .where(
                JPAExpressions
                    .select(stSub.study.id)
                    .from(stSub)
                    .leftJoin(stSub.tag, tSub)
                    .where(stSub.study.eq(study).and(tSub.name.in(tags)))
                    .groupBy(stSub.study.id)
                    .having(stSub.id.countDistinct().eq((long) tags.size()))
                    .exists()
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    // title 포함
    private BooleanExpression titleContains(String type, String keyword) {
        if (!"title".equals(type) || keyword == null || keyword.isEmpty()) {
            return null;
        }
        return QStudy.study.title.containsIgnoreCase(keyword);
    }
    // content 포함
    private BooleanExpression contentContains(String type, String keyword) {
        if (!"content".equals(type) || keyword == null || keyword.isEmpty()) {
            return null;
        }
        return QStudy.study.content.containsIgnoreCase(keyword);
    }
    // createdBy 포함
    private BooleanExpression writerContains(String type, String keyword) {
        if (!"createdBy".equals(type) || keyword == null || keyword.isEmpty()) {
            return null;
        }
        return QStudy.study.member.username.containsIgnoreCase(keyword);
    }
    // category 포함
    private BooleanExpression categoryEq(Long categoryId) {
        if (categoryId == null || categoryId == 0) {
            return null;
        }
        return QStudy.study.category.id.eq(categoryId);
    }
}
