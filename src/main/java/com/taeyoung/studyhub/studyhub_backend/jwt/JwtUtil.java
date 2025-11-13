package com.taeyoung.studyhub.studyhub_backend.jwt;

import com.taeyoung.studyhub.studyhub_backend.domain.member.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {

    static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);;

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("id", user.id)
                .claim("username", user.getUsername())
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000)) //유효기간 100초
                .signWith(key)
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }
}
