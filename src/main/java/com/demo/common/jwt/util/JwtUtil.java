package com.demo.common.jwt.util;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//0.12.3 버전 구현 방식
@Component
public class JwtUtil {

    private SecretKey secretKey;

    private Long expiredTime;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expire_time}") Long expiredTime) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiredTime = expiredTime;
    }

    /*토큰 생성*/
    public String createJwt(String userId, String role) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                //발행시간
                .issuedAt(new Date(System.currentTimeMillis()))
                //만료시간
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(secretKey)
                .compact();
    }

    /*사용자 아이디 찾기*/
    public String getUserId(String token) {
        //토큰이 우리서버에서 생성되었는지 파악
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);
    }
    /*사용자 role*/
    public String getRole(String token) {
        //토큰이 우리서버에서 생성되었는지 파악
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    /*토큰 검증*/
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
