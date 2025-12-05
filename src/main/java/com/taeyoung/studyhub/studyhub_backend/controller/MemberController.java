package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import com.taeyoung.studyhub.studyhub_backend.domain.member.Member;
import com.taeyoung.studyhub.studyhub_backend.dto.exception.ErrorResponseDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.EmailRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.auth.jwt.JwtUtil;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.UsernameAndPasswordRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.find.UsernameRequestDto;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody SignupRequestDto signupRequestDto){
        memberService.registerMember(signupRequestDto);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();

        try {
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateMyInfo(@Valid @RequestBody UpdateRequestDto updateRequestDto, BindingResult bindingResult, Authentication authentication){
        // password, email 필수 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            memberService.updateMember(updateRequestDto, userId);

            return ResponseEntity.ok("회원수정 성공!");

        } catch (IllegalArgumentException e) {
            // 회원이 존재하지 않거나 검증 실패
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            // 기타 서버 오류
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyAccount(Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            memberService.deleteMember(userId);
            return ResponseEntity.ok("회원탈퇴 성공!");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("이미 삭제되었거나 존재하지 않는 사용자입니다.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("연관된 데이터가 있어 회원을 삭제할 수 없습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
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
