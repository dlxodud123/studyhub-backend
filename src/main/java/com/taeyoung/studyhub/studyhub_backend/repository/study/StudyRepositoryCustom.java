package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StudyRepositoryCustom {
    Page<Study> searchStudies(String searchType, String keyword, Long categoryId, Pageable pageable);
}
