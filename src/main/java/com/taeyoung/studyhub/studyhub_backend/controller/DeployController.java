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
    public String deploy4() {
        return "github action success";
    }

    @GetMapping("/deploy/5")
    public String deploy5() {
        return "최종 확인!";
    }

    @GetMapping("/deploy/6")
    public String deploy6() {
        return "최종 확인! 진짜로! nginx 적용";
    }

    @GetMapping("/deploy/7")
    public String deploy7() {
        return "축하합니다!";
    }

    @GetMapping("/deploy/8")
    public String deploy8() {
        return "축하합니다!진짜로!";
    }

    @GetMapping("/deploy/9")
    public String deploy9() {
        return "축하합니다!진짜로!asdf";
    }

    @GetMapping("/deploy/10")
    public String deploy10() {
        return "축하합니다!진짜로!asdf101010";
    }

    @GetMapping("/deploy/11")
    public String deploy11() {
        return "축하합니다!진짜로!asdf11111111111111111";
    }

    @GetMapping("/deploy/12")
    public String deploy12() {
        return "축하합니다!진짜로!12";
    }

    @GetMapping("/deploy/success")
    public String deploySuccess() {
        return "마지막 배포 테스트";
    }
}
