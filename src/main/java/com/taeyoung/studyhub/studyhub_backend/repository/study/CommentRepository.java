package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudyId(Long studyId);
}
