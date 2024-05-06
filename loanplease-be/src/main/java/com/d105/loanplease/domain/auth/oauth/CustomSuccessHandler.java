package com.d105.loanplease.domain.auth.oauth;

import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//    private final JWTUtil jwtUtil;

    private final TokenProvider tokenProvider;

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
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            String email = oauthUser.getName();  // OAuth2 로그인에서 제공받은 이메일
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                //이메일이 DB에 존재하는 경우, 홈 페이지로 리다이렉트
                response.sendRedirect("/home");
            } else {
                // 새 사용자 등록
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setNickname(oauthUser.getName());
                newUser.setProfileImg(oauthUser.getProfilePictureUrl());
                newUser.setRole("USER");  // 기본 권한 설정
                userRepository.save(newUser);

                // 토큰 생성
                String accessToken = tokenProvider.createAccessJwt(newUser.getEmail(), newUser.getRole());
                String refreshToken = tokenProvider.createRefreshJwt(newUser.getEmail());

                // 토큰 저장
                Token newToken = new Token(newUser.getEmail(), refreshToken, accessToken);
                tokenRepository.save(newToken);

                // 쿠키 설정
                addCookieToResponse(response, "Authorization", accessToken, accessMaxAge);
                addCookieToResponse(response, "RefreshToken", refreshToken, refreshMaxAge);

                // 사용자 등록 페이지 또는 환영 페이지로 리다이렉트
                response.sendRedirect("/welcome");
            }
        } catch (Exception e) {
            logger.error("Authentication Success Handler Error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
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