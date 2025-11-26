package com.taeyoung.studyhub.studyhub_backend.domain.study;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public StudyTag() {
    }

    public StudyTag(Study study, Tag tag) {
        this.study = study;
        this.tag = tag;
    }
}
