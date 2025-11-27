package com.taeyoung.studyhub.studyhub_backend.domain.study;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy="tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyTag> studyTags = new ArrayList<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // StudyTag 편의 메서드
    public void addStudyTag(StudyTag studyTag) {
        studyTags.add(studyTag);
        studyTag.setTag(this);
    }
    public void removeStudyTag(StudyTag studyTag) {
        studyTags.remove(studyTag);
        studyTag.setTag(null);
    }
}