package com.taeyoung.studyhub.studyhub_backend.domain.study;

import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy="study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyTag> studyTags = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Study() {
    }

    public Study(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void editStudy(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Study(String title, String content, Member member, Category category) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
    }

    // StudyTag 편의 메서드
    public void addStudyTag(StudyTag studyTag) {
        studyTags.add(studyTag);
        studyTag.setStudy(this); // StudyTag 안의 study 필드도 세팅
    }
    public void removeStudyTag(StudyTag studyTag) {
        studyTags.remove(studyTag);
        studyTag.setStudy(null); // 연관관계 끊기
    }
}
