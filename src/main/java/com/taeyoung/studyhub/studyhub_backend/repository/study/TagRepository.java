package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
