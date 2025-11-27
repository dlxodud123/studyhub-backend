package com.taeyoung.studyhub.studyhub_backend.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -704561232L;

    public static final QMember member = new QMember("member1");

    public final com.taeyoung.studyhub.studyhub_backend.global.entity.QBaseEntity _super = new com.taeyoung.studyhub.studyhub_backend.global.entity.QBaseEntity(this);

    public final ListPath<com.taeyoung.studyhub.studyhub_backend.domain.study.Comment, com.taeyoung.studyhub.studyhub_backend.domain.study.QComment> comments = this.<com.taeyoung.studyhub.studyhub_backend.domain.study.Comment, com.taeyoung.studyhub.studyhub_backend.domain.study.QComment>createList("comments", com.taeyoung.studyhub.studyhub_backend.domain.study.Comment.class, com.taeyoung.studyhub.studyhub_backend.domain.study.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final EnumPath<ProviderType> provider = createEnum("provider", ProviderType.class);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final ListPath<com.taeyoung.studyhub.studyhub_backend.domain.study.Study, com.taeyoung.studyhub.studyhub_backend.domain.study.QStudy> studies = this.<com.taeyoung.studyhub.studyhub_backend.domain.study.Study, com.taeyoung.studyhub.studyhub_backend.domain.study.QStudy>createList("studies", com.taeyoung.studyhub.studyhub_backend.domain.study.Study.class, com.taeyoung.studyhub.studyhub_backend.domain.study.QStudy.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

