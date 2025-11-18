package com.taeyoung.studyhub.studyhub_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyController {

    @PostMapping("/api/studies")
    public void findPassword() {

    }
}
