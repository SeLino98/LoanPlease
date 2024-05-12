package com.d105.loanplease.domain.auth.jwt;

import com.d105.loanplease.domain.auth.entity.Token;
import com.d105.loanplease.domain.auth.repository.TokenRepository;
import com.d105.loanplease.global.util.RedisUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.Option;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //유저 레포

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisUtility redisUtility;
    private final TokenRepository tokenRepository;
    private static final String AUTHORITIES_KEY = "auth";
    private static SecretKey secretKey;
    private static final Long accessExpiredMs = 60*60*60L;
    private static final int accessMaxAge = 60*60;
    private static final Long refreshExpiredMs = 604800*1000L;
    private static final int refreshMaxAge = 604800; //7일;

    @Value("${spring.jwt.access.header}")
    private String accessHeader;
    @Value("${spring.jwt.refresh.header}")
    private String refreshHeader;

    private final Long tokenValidityInMilliseconds;

    public TokenProvider(AuthenticationManagerBuilder authenticationManagerBuilder,
                         StringRedisTemplate stringRedisTemplate,
                         @Value("${spring.jwt.secret}")  String secret,
                         @Value("${spring.jwt.token-validity-in-seconds}") Long tokenValidityInMilliseconds,
                         RedisUtility redisUtility,
                         TokenRepository tokenRepository) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisUtility = redisUtility;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.tokenRepository = tokenRepository;
    }
//    public Authentication getAuthentication(String accessToken) {
//        Claims claims = parseClaims(accessToken);
//
//        Collection<? extends GrantedAuthority> authorities = Arrays
//                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        User principal = new User(claims.getSubject(), "", authorities);
//
//        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
//    }
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); // Ensure this method uses the updated parseClaims

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    //토큰을 추출한다.
    public Optional<String> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(accessHeader))
                .map(accessToken -> accessToken.substring(7));
    }
//    public Optional<String> extractRefreshToken(HttpServletRequest request){
//        return Optional.ofNullable(request.getHeader(refreshHeader))
//                .map(refreshToken -> refreshToken.substring(7));
//    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(refreshHeader);
        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            return Optional.of(headerValue.substring(7)); // 'Bearer ' 다음부터 시작하는 토큰 추출
        }
        return Optional.empty(); // 헤더가 null이거나 'Bearer '로 시작하지 않는 경우
    }

    //토큰에서 사용자 정보 추출
    public String extractSubject(String accessToken){
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }
//    private Claims parseClaims(String accessToken){
//        try{
//            return Jwts.parser()
//                    .verifyWith(secretKey).build().parseClaimsJwt(accessToken).getBody();
//        }catch (ExpiredJwtException e){
//            return e.getClaims();
//        }
//    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey).build().parseClaimsJws(accessToken).getBody(); // Use parseClaimsJws for consistency
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // Handle expired JWTs specifically
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT signature"); // Adding handling for signature issues
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token"); // Handling other parsing exceptions
        }
    }

    public static boolean isExpired(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    //엑세스 토큰 만들기
    public String createAccessJwt(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now+tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(authentication.getName()).claim(AUTHORITIES_KEY,authorities)
                .issuedAt(new Date(System.currentTimeMillis())) //발행시간
                .expiration(accessTokenValidity) //만료 시간
                .signWith(secretKey) //비밀키
                .compact(); //
    }

    //리프레쉬 토큰을 만들기
    public String createRefreshJwt(String email) {
        // 리프레시 토큰은 액세스 토큰보다 정보가 적게 필요합니다.
        // 여기서는 이메일만 포함하고 만료 시간을 길게 설정합니다.
        long now = (new Date()).getTime();
        Date refreshTokenValidity = new Date(now + tokenValidityInMilliseconds *21);
        return  Jwts.builder()
                .claim("email",email) //리프레시 토큰 주체의 식별 정보
                .issuedAt(new Date(System.currentTimeMillis()))//토큰 발행 시간.
                .expiration(refreshTokenValidity) //토큰 만료 시간.
                .signWith(secretKey)
                .compact();
    }

    //토큰이 유효한지 검사한다.
    public boolean isTokenValid(String accessToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken); // Change to parseClaimsJws for signature verification
            return true;
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token: {}", e.getMessage());
            return false; // Handling expired token
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return false; // Handling invalid signature
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false; // Handling other exceptions
        }
    }

    //리프레쉬 토큰을 DB에 업데이트된 토큰으로 저장한다.
    public void updateTokenRepo(String email, String refreshToken, String accessToken){
        Token updatedToken = Token.builder().id(email).refreshToken(refreshToken).accessToken(accessToken).build();
        tokenRepository.save(updatedToken);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        String newAccessToken = createAccessJwt(getAuthentication(extractAccessToken(request).orElse(null)));
        tokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(token -> {
                    String newRefreshToken = updateRefreshToken(newAccessToken);
                    //tokenProvider.updateTokenRepo(token.getId(), newR efreshToken, tokenProvider.createAccessToken(authentication)
                    System.out.println("save new Token to TokenRepository ( Redis )");
                    updateTokenRepo(token.getId(), newRefreshToken, newAccessToken);
                    System.out.println(tokenRepository.findById(token.getId()));
                    response.setHeader(accessHeader, newAccessToken);
                    response.setHeader(refreshHeader, newRefreshToken);
                });
    }

    //리프세쉬토큰을 업데이트한다.
    private String updateRefreshToken(String accessToken){
        String newRefreshToken = createRefreshJwt(extractSubject(accessToken));
        updateTokenRepo(extractSubject(accessToken),newRefreshToken,accessToken);
        return newRefreshToken;
    }


}

//    public String getEmail(String token) {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
//    }
//
//    public String getRole(String token) {
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
//    }

//    public Boolean isExpired(String token) {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
//    }