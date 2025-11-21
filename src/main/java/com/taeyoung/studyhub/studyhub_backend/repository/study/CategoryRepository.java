package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
