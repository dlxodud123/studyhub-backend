package com.taeyoung.studyhub.studyhub_backend.domain.member;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Comment;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import com.taeyoung.studyhub.studyhub_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Study> studies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Member() {
    }

    public Member(String username, String password, String email, ProviderType provider, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
    }

    public void updateMember(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public void setRandomPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role){
        this.role = role;
    }
}

