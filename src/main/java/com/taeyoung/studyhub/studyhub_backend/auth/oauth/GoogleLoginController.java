package com.taeyoung.studyhub.studyhub_backend.auth.oauth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleOAuthService googleOAuthService;
    private final GoogleLoginService googleLoginService;

    // 구글 로그인 버튼 클릭 → 로그인 URL로 리다이렉트
    @GetMapping("/oauth/google/login")
    public String login() {
        return "redirect:" + googleOAuthService.getLoginUrl();
    }

    // 구글 인증 후 콜백
    @GetMapping("/oauth/google/callback")
    public String callback(@RequestParam String code, HttpServletResponse response, Model model) {
        String accessToken = googleOAuthService.getAccessToken(code);
        Map<String, Object> userInfo = googleOAuthService.getUserInfo(accessToken);
        String email = (String) userInfo.get("email");
        String username = (String) userInfo.get("id");

        model.addAttribute("email", email);
        // email을 활용해 DB로 중복 체크
        if (googleLoginService.checkEmailExists(email)){
            return "redirect:/login";
        }

        System.out.println("email boolean : " + googleLoginService.checkEmailExists(email));
        System.out.println("username boolean : " + googleLoginService.checkUsernameExists(username));

        // username 활용해 DB로 중복 체크
        if (googleLoginService.checkUsernameExists(username)){
            model.addAttribute("readonlyUsername", false);
        } else {
            model.addAttribute("username", username);
            model.addAttribute("readonlyUsername", true);
        }

        return "socialSignup";
    }
}