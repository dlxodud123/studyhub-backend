package com.taeyoung.studyhub.studyhub_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeployController {

    @GetMapping("/deploy")
    public String deploy() {
        return "배포 성공!";
    }

    @GetMapping("/deploy/2/{number}")
    public Long deploy2(@PathVariable Long number) {
        return number;
    }

    @GetMapping("/deploy/3/{number}")
    public Long deploy3(@PathVariable Long number) {
        return number;
    }

    @GetMapping("/deploy/4")
    public String deploy3() {
        return "github action success";
    }
}
