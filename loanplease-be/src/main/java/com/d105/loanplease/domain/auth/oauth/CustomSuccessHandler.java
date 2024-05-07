package com.d105.loanplease.domain.auth.oauth;

import com.d105.loanplease.domain.auth.entity.Token;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;


@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//    private final JWTUtil jwtUtil;

    private final TokenProvider tokenProvider;
    private final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);
    @Autowired
    private UserRepository userRepository;

    private static final Long accessExpiredMs = 60*60*60L;
    private static final int accessMaxAge = 60*60;

    private static final Long refreshExpiredMs = 604800*1000L;
    private static final int refreshMaxAge = 604800; //7일;

    public CustomSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {


            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal(); //구글을 통해 받은 값.
            String email = oauthUser.getName();  // OAuth2을 통해 제공받은 이메일


            logger.info("AAA");
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                //이메일이 DB에 존재하는 경우, 홈 페이지로 리다이렉트
                response.sendRedirect("https://loanplease.kr/");
//                response.sendRedirect("http://localhost:8080/");

                log.info("이미존재쓰<>");

            } else {
                // 새 사용자라면? 등록
                // 밑에 이 부분이 은행원으로 시작하기 했을 때 DB에 등록되는 코드로 해야된다.
                //
                logger.info("AAA");
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setNickname("당신의 멋진 닉네임");
                newUser.setProfileImg(oauthUser.getPicture());
                newUser.setRole("USER");  // 기본 권한 설정
//                userRepository.save(newUser);
                logger.info("AAA");
                // 토큰 생성

                String accessToken = tokenProvider.createAccessJwt(authentication);
                String refreshToken = tokenProvider.createRefreshJwt(newUser.getEmail());
                logger.info("BBB");
                // 토큰 저장
//                tokenProvider.updateTokenRepo(newUser.getEmail(), refreshToken, accessToken);
                logger.info("CCC");
                // 쿠키 설정
                //return access Token
//                response.addCookie(createCookie("Authorization", accessToken));
//                //return refresh Token
//                response.addCookie(createHttpOnlyCookie("RefreshToken",refreshToken));
                logger.info("DDD");
                // 사용자 정보 전송
//                 JSON 형태로 응답
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(newUser));

                // 사용자 등록 페이지 리다이렉트
                response.sendRedirect("https://loanplease.kr/");
//                response.sendRedirect("http://localhost:5173/");
            }
        } catch (Exception e) {
            logger.error("Authentication Success Handler Error: {}", e );
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
    private Cookie createHttpOnlyCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(604800);  // 여기서는 리프레시 토큰의 유효 기간을 설정
        cookie.setPath("/");
        cookie.setHttpOnly(true);  // JS를 통한 접근 방지
        return cookie;
    }
}