package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.EmailRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.UsernameAndPasswordRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.UsernameRequestDto;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody SignupRequestDto signupRequestDto){
        memberService.registerMember(signupRequestDto);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<CustomUser> getMyInfo(Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        return ResponseEntity.ok(user);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyAccount(Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        memberService.deleteMember(user.getId());
        return ResponseEntity.ok("회원탈퇴 성공!");
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateMyInfo(@Valid @RequestBody UpdateRequestDto updateRequestDto, Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        memberService.updateMember(updateRequestDto, user.getId());
        return ResponseEntity.ok("회원수정 성공!");
    }

    // username 찾기
    @PostMapping("/find-username")
    public ResponseEntity<String> findUsername(@RequestBody EmailRequestDto emailRequestDto) {
        String findUsername = memberService.findByUsernameByEmail(emailRequestDto.getEmail());

        return ResponseEntity.ok(findUsername);
    }

    // password 찾기
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@RequestBody UsernameRequestDto usernameRequestDto) {
        String findPassword = memberService.findByPasswordByUsername(usernameRequestDto.getUsername());

        return ResponseEntity.ok(findPassword);
    }

    // email 찾기
    @PostMapping("/find-email")
    public ResponseEntity<String> findEmail(@RequestBody UsernameAndPasswordRequestDto usernameAndPasswordRequestDto) {
        String findEmail = memberService.findByEmailByUsernameAndPassword(
                usernameAndPasswordRequestDto.getUsername(),
                usernameAndPasswordRequestDto.getPassword()
        );

        return ResponseEntity.ok(findEmail);
    }
}
