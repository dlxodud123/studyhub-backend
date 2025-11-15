package com.taeyoung.studyhub.studyhub_backend.auth.oauth;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
public class GoogleOAuthService {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    // 로그인 URL 생성
    public String getLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl(GOOGLE_AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .build().toUriString();
    }

    // code -> access_token
    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        Map<String, Object> response = restTemplate.postForObject(GOOGLE_TOKEN_URL, request, Map.class);
        return (String) response.get("access_token");
    }

    // access_token -> 사용자 정보 조회
    public Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_USERINFO_URL + "?access_token=" + accessToken;
        return restTemplate.getForObject(url, Map.class);
    }
}
