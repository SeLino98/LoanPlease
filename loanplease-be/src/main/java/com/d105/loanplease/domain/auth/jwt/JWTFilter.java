package com.d105.loanplease.domain.auth.jwt;

import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.auth.oauth.CustomOAuth2User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    //허용 Uri를 관리하는 메서드
    private boolean isAllowedPath(String requestUri){
        List<String> allowedPaths = Arrays.asList("/api/server", "/api/upload", "/swagger-ui/","/api/refresh");
        return allowedPaths.stream().anyMatch(p -> requestUri.startsWith(p));
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            if (isAllowedPath(request.getRequestURI())){ //허용된 URI인지 확인한다.
                //특정 경로에 대해 필터링 없이 진행한다.
                filterChain.doFilter(request,response);
                return;
            }
            //특정 경로가 아니면 Access 토큰을 추출한다.
//            String accessToken =



        }catch (ExpiredJwtException e){
            //401에러


        }

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if(cookies==null) {
            log.info("현재 cookies가 null입니다.");
            filterChain.doFilter(request, response);
            return;   //배포 시 예외처리
        }

        // 특정 경로 요청은 필터링 없이 진행
//        if (request.getRequestURI().startsWith("/api/server")) {
//            log.info("JWT Filter test uri 입니다.");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if (request.getRequestURI().startsWith("/api/upload")) {
//            log.info("JWT Filter test uri 입니다.");
//            filterChain.doFilter(request, response);
//            return;
//        }
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        if (tokenProvider.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 email과 role 획득
        String email = tokenProvider.getEmail(token);
        String role = tokenProvider.getRole(token);

        //userDTO를 생성하여 값 set
        User userDTO = new User();
        userDTO.setEmail(email);
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

//    private String updateRefreshToken(String accessToken) {
//        String newRefreshToken = tokenProvider.
//                createRefreshJwt(tokenProvider.extractSubject(accessToken));
//        tokenProvider.updateTokenRepo(tokenProvider.extractSubject(accessToken), newRefreshToken, accessToken);
//        return newRefreshToken;
//    }



    //인가되지 않은 사용자에게 띄어줄 페이지
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }



}