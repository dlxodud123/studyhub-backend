package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
