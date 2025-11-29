package com.taeyoung.studyhub.studyhub_backend.repository.study;

import com.taeyoung.studyhub.studyhub_backend.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface StudyRepositoryCustom {
    Page<Study> searchStudies(String searchType, String keyword, Long categoryId, Pageable pageable);
    Page<Study> searchStudiesByTags(List<String> tags, Long categoryId, Pageable pageable);
}
