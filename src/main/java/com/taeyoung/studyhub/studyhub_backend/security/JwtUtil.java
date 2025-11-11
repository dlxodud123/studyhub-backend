package com.taeyoung.studyhub.studyhub_backend.security;

import com.taeyoung.studyhub.studyhub_backend.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;

    // JWT 발급 메서드
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    // JWT 검증 메서드
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
}
