package com.taeyoung.studyhub.studyhub_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    // member
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "member/signup";
    }

    @GetMapping("/my-page")
    public String myPage(){
        return "member/mypage.html";
    }

    @GetMapping("/modify")
    public String modify(){
        return "member/modify.html";
    }

    @GetMapping("/find-email")
    public String findEmail(){
        return "member/findemail.html";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "member/findpassword.html";
    }

    @GetMapping("/find-username")
    public String findUsername(){
        return "member/findusername.html";
    }

    // study
    @GetMapping("/study/list")
    public String studyList(){
        return "study/studylist.html";
    }

    @GetMapping("/study/create")
    public String studyCreate(){
        return "study/studycreate.html";
    }

    @GetMapping("/studies/{id}")
    public String studyDetail(){return "study/studydetail.html";}

    @GetMapping("/study/edit/{id}")
    public String studyEdit(){return "study/studyedit.html";}

    // admin
    @GetMapping("/admin/dashboard")
    public String adminDashboard(){return "admin/dashboard.html";}

    @GetMapping("/admin/members")
    public String adminMembers(){return "admin/members.html";}

    @GetMapping("/admin/studies")
    public String adminStudies(){return "admin/studies.html";}
}