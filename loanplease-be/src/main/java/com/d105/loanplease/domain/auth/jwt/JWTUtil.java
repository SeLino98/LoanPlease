package com.d105.loanplease.domain.auth.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createAccessJwt(String email, String role, Long expiredMs) {
        //ACCESS TOken
        return Jwts.builder()
                .claim("email", email) //내가 누구인지
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) //발행시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) //만료 시간
                .signWith(secretKey) //비밀키
                .compact(); //
    }

    //리프레쉬 토큰을 만들기
    public String createRefreshJwt(String email, Long refreshExpMs) {
        // 리프레시 토큰은 액세스 토큰보다 정보가 적게 필요합니다.
        // 여기서는 이메일만 포함하고 만료 시간을 길게 설정합니다.
        return  Jwts.builder()
                .claim("email",email) //리프레시 토큰 주체의 식별 정보
                .issuedAt(new Date(System.currentTimeMillis()))//토큰 발행 시간.
                .expiration(new Date(System.currentTimeMillis()+refreshExpMs)) //토큰 만료 시간.
                .signWith(secretKey)
                .compact();
    }



}
