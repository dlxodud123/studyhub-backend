package com.taeyoung.studyhub.studyhub_backend.domain.study;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Study extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id")
//    private Category category;

//    @ManyToMany
//    @JoinTable(
//            name = "study_tag",
//            joinColumns = @JoinColumn(name = "study_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    private List<Tag> tags = new ArrayList<>();

    public Study() {
    }

    public Study(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
