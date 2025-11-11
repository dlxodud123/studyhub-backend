package com.taeyoung.studyhub.studyhub_backend.security;

import com.taeyoung.studyhub.studyhub_backend.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
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

    /// JWT 검증: 유효하면 true, 아니면 false
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(secretKey) // Key 객체 그대로 사용
//                    .(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }

    // JWT 까주는 함수
//    public static Claims extractToken(String token) {
//        Claims claims = Jwts.parser().verifyWith(key).build()
//                .parseSignedClaims(token).getPayload();
//        return claims;
//    }

    // JWT에서 username 추출
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(jwtConfig.getSecretKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
}
