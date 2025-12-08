package com.taeyoung.studyhub.studyhub_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeployController {

    @GetMapping("/deploy")
    public String deploy() {
        return "배포 성공!";
    }
}
