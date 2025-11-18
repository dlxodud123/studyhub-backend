package com.taeyoung.studyhub.studyhub_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/my-page")
    public String myPage(){
        return "mypage.html";
    }

    @GetMapping("/modify")
    public String modify(){
        return "modify.html";
    }

    @GetMapping("/find-email")
    public String findEmail(){
        return "findemail.html";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "findpassword.html";
    }

    @GetMapping("/find-username")
    public String findUsername(){
        return "findusername.html";
    }
}