package com.koreait.surl_project_11.domain.auth.auth.service;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.global.app.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    public String genToken(Member member, long expireSeconds) {
        Claims claims = Jwts.claims()
                .add("id", member.getId())
                .add("username", member.getUsername())
                .build();
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expireSeconds * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getDataFrom(String token) {
        Claims payload = Jwts.parser()
                .setSigningKey(AppConfig.getJwtSecretKey())
                .build()
                .parseClaimsJws(token).getPayload();
        return Map.of(
                "id", payload.get("id", Integer.class),
                "username", payload.get("username", String.class)
        );
    }
}
