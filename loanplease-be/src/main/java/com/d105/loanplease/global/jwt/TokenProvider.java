package com.d105.loanplease.global.jwt;

import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.cipher.HashUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Slf4j
@Component
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //유저 레포
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    private final RedisService redisService;

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

    public TokenProvider(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                         StringRedisTemplate stringRedisTemplate, UserRepository userRepository,
                         @Value("${spring.jwt.secret}")  String secret,
                         @Value("${spring.jwt.token-validity-in-seconds}") Long tokenValidityInMilliseconds,
                         RedisService redisService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); // Ensure this method uses the updated parseClaims
        logger.info(claims.getSubject()+"ASDFASDF");
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        User principal = new User(claims.getSubject(), "", authorities);
//        User principal = userRepository.findByEmail()
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    //토큰을 추출한다.
    public Optional<String> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(accessHeader))
                .map(accessToken -> accessToken.substring(7));
    }
    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .map(accessToken -> accessToken.substring(7));
    }

//    public Optional<String> extractEmail(String refreshToken) { // 엑세스 토큰으로 refresh토큰을 생성한다. 후에 refresh 토큰을 redis에 저장된다.
//        logger.info(refreshToken+"refreshToken");
//        String userEmail = redisService.getValues(refreshToken);
////        String getRedisToken = redisService.getValues()
//        logger.info(userEmail+"EMAIL");
//        logger.info(refreshToken+"RefreshToken");
//        if (!userEmail.isEmpty()) {
//            if (HashUtil.verify(refreshToken,userEmail)){ //해쉬값이 일치하면 이메일을 준다.
//                return Optional.of(userEmail);
//            } // 'Bearer' 다음부터 시작하는 토큰 추출
//        }
//        return Optional.empty(); // 헤더가 null이거나 'Bearer '로 시작하지 않는 경우
//    }

    //토큰에서 사용자 정보 추출
    public String extractSubject(String accessToken){
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

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

    //엑세스 토큰 만들기
    public String createAccessJwt(String email) {
        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now+tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(email) //
                .issuedAt(new Date(System.currentTimeMillis())) //발행시간
                .expiration(accessTokenValidity) //만료 시간
                .signWith(secretKey) //비밀키
                .compact(); //
    }
    public String createRefreshJwt(String accessToken, String email) {
        long now = (new Date()).getTime();
        Date refreshTokenValidity = new Date(now + tokenValidityInMilliseconds * 21);

        String refreshToken = Jwts.builder()
                .claim("email", accessToken)
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(refreshTokenValidity)
                .signWith( secretKey)
                .compact();

        String hashedToken = HashUtil.hash(refreshToken);
        redisService.setValues(hashedToken, email);
        return refreshToken;
    }

    public Optional<String> extractEmail(String refreshToken) {
        logger.info(refreshToken + " refreshToken");

        // Redis에서 저장된 모든 키를 검색하여 해시된 토큰을 찾기
        Set<String> keys = redisService.getKeys("*");
        for (String key : keys) {
            if (HashUtil.verify(refreshToken, key)) {
                String userEmail = redisService.getValues(key);
                if (userEmail != null && !userEmail.isEmpty()) {
                    return Optional.of(userEmail);
                }
            }
        }
        return Optional.empty();
    }

//    public String createRefreshJwt(String accessToken,String email) {
//
//        // 리프레시 토큰은 액세스 토큰보다 정보가 적게 필요합니다.
//        // 여기서는 이메일만 포함하고 만료 시간을 길게 설정합니다.
//        // 레디스에 저장될 때 암호화하여 저장합니다.
//
//        long now = (new Date()).getTime();
//        Date refreshTokenValidity = new Date(now + tokenValidityInMilliseconds *21);
//        String refreshToken =Jwts.builder()
//                .claim("email",accessToken) //리프레시 토큰 주체의 식별 정보
//                .issuedAt(new Date(System.currentTimeMillis()))//토큰 발행 시간.
//                .expiration(refreshTokenValidity) //토큰 만료 시간.
//                .signWith(secretKey)
//                .compact();
//        String hashedToken = HashUtil.hash(refreshToken);
//        redisService.setValues(hashedToken,email);
//        return  refreshToken;
//    }
//    public String createRefreshJwt(String accessToken) {
//
//        // 리프레시 토큰은 액세스 토큰보다 정보가 적게 필요합니다.
//        // 여기서는 이메일만 포함하고 만료 시간을 길게 설정합니다.
//        // 레디스에 저장될 때 암호화하여 저장합니다.
//
//        long now = (new Date()).getTime();
//        Date refreshTokenValidity = new Date(now + tokenValidityInMilliseconds *21);
//        String refreshToken =
//        String hashedToken = HashUtil.hash()
//
//        return  Jwts.builder()
//                .claim("email",accessToken) //리프레시 토큰 주체의 식별 정보
//                .issuedAt(new Date(System.currentTimeMillis()))//토큰 발행 시간.
//                .expiration(refreshTokenValidity) //토큰 만료 시간.
//                .signWith(secretKey)
//                .compact();
//    }

    public boolean isValidRefreshToken(String refreshToken){
//        String extractRefreshToken = redisService.getValues(refreshToken);
//        if (HashUtil.verify(refreshToken,extractRefreshToken)){
//            return extractRefreshToken;
//        }
        boolean isValid = false;
        // Redis에서 저장된 모든 키를 검색하여 해시된 토큰을 찾기
        Set<String> keys = redisService.getKeys("*");
        for (String key : keys) {
            if (HashUtil.verify(refreshToken, key)) {
                String userEmail = redisService.getValues(key);
                if (userEmail != null && !userEmail.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    // 토큰이 유효한지 검사한다.
    public boolean isTokenValid(String accessToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

//    //리프레쉬 토큰을 REdis에 업데이트한다.
//    public String updateTokenRepo(String email, String accessToken){
////        String newRefreshToken = createRefreshJwt(extractSubject(accessToken));
//        String newRefreshToken = createRefreshJwt(accessToken);
//        createRefreshJwt(accessToken,email);
//
//
//        return newRefreshToken;
//    }

    //프론트에서 보낸 리프레쉬 토큰의 정보를 통해 업데이트한다.
//    public void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
//        String email = request.getHeader("RefreshToken");
//        //리프레쉬 토큰을 이용해서 엑세스 토큰을 새로 발급을 하고,
//        //리프레쉬 토큰을 redis에 재갱신한다.
////        String myRefreshToken = redisService.getValues(refreshToken);
//        String myRefreshToken = getRefreshToken(refreshToken);
//
//        if (myRefreshToken.equals("InValid")){
//            //리프레쉬 토큰이 없는 경우이다. 재로그인 해야 된다.
//        }else{
//            //엑세스 토큰과 리프레쉬 토큰을 발급해준다.
//            String newAccessToken = createAccessJwt(email);
//            String newRefreshToken = createRefreshJwt(newAccessToken, email);
//            response.setHeader(accessHeader, newAccessToken);
//            response.setHeader(refreshHeader, newRefreshToken);
//        }
//    }
}