package com.taeyoung.studyhub.studyhub_backend.domain.study;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Study> studies = new ArrayList<>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}
