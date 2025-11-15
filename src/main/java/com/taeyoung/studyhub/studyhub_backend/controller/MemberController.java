package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.UpdateRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.response.MemberResponseDto;
import com.taeyoung.studyhub.studyhub_backend.auth.jwt.JwtUtil;
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

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인
    @PostMapping("/api/members/login")
    public ResponseEntity<String> loginMember(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult bindingResult, HttpServletResponse response){
        // username, password, email 필수 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Authentication 객체를 생성하고 SecurityContext에 적용
        var authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Authentication는 스레드 로컬을 사용하므로 각 요청마다 독립적이다.
        // Authentication에 auth를 createToken메서드에 보냄
        var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());

        var cookie = new Cookie("jwt", jwt);
        //  JWT만들었을때의 기간이랑 같게
        cookie.setMaxAge(1000);
        //  쿠키를 자바스크립트로 조작 못하게
        cookie.setHttpOnly(true);
        //  쿠키가 전송될 URL
        cookie.setPath("/");
        //  브라우저에 저장
        response.addCookie(cookie);

        return ResponseEntity.ok(jwt);
    }

    // 회원가입
    @PostMapping("/api/members/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult){
        // username, password, email 필수 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            memberService.registerMember(signupRequestDto);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }

    // 회원 정보 조회
    @GetMapping("/api/members/me")
    public ResponseEntity<?> getMyInfo(Authentication authentication){
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            MemberResponseDto dto = memberService.getMyInfo(userId);
            return ResponseEntity.ok(dto);
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
    @PutMapping("/api/members/update")
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
    @DeleteMapping("/api/members/delete")
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
}
