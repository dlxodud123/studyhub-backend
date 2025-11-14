package com.taeyoung.studyhub.studyhub_backend.controller;

import com.taeyoung.studyhub.studyhub_backend.dto.member.request.LoginRequestDto;
import com.taeyoung.studyhub.studyhub_backend.dto.member.request.SignupRequestDto;
import com.taeyoung.studyhub.studyhub_backend.jwt.JwtUtil;
import com.taeyoung.studyhub.studyhub_backend.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
        }
    }

    // 회원 정보 조회
    @GetMapping("/api/members/me")
    public String getMyInfo(){
//        memberService.getMyInfo();
        return "me";
    }

    // 회원 정보 수정
    @PutMapping("/api/members/me")
    public String updateMyInfo(){
//        memberService.updateMember();
        return "update";
    }

    // 회원 탈퇴
    @DeleteMapping("/api/members/delete")
    public ResponseEntity<String> deleteMyAccount(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    Long userId = Long.valueOf(JwtUtil.extractToken(jwtToken).get("id").toString());
                    System.out.println("쿠키 id : " + userId);
                    try {
                        memberService.deleteMember(userId);
                        return ResponseEntity.ok("회원탈퇴 성공!");
                    } catch (RuntimeException e) {
                        // 서비스에서 던진 메시지 그대로 내려줌
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(e.getMessage());
                    }
                }
            }
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("로그인 정보(JWT)가 없습니다.");
    }
}
