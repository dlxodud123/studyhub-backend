package com.taeyoung.studyhub.studyhub_backend.service;

import com.taeyoung.studyhub.studyhub_backend.domain.member.ProviderType;
import com.taeyoung.studyhub.studyhub_backend.domain.study.Category;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.study.request.StudyCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdminServiceTest {

    @BeforeEach
    public void before() {
    }


    @Test
    public void getDashboard() {
    }

    @Test
    public void getMembers() {
    }

    @Test
    public void memberDelete() {
    }

    @Test
    public void memberChangeRole() {
    }

    @Test
    public void getStudies() {
    }

    @Test
    public void studyDelete() {
    }


}
