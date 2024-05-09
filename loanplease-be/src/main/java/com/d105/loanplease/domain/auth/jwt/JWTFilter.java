package com.d105.loanplease.domain.auth.jwt;

import com.d105.loanplease.domain.auth.repository.TokenRepository;
import com.d105.loanplease.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
//    Ant 스타일 패턴 매칭 사용
//    Spring에서는 Ant 스타일의 경로 매칭을 지원하여 보다 유연하게 URL 패턴을 처리할 수 있습니다.
//    PathMatcher와 같은 클래스를 사용하여 Ant 스타일의 패턴을 사용할 수 있습니다.
    //허용 Uri를 관리하는 메서드
    private boolean isAllowedPath(String requestUri){
        List<String> allowedPaths = Arrays.asList("/api/server", "/api/upload", "/api/auth/nickname/**" ,"/swagger-ui/","/api/refresh","/api/auth/register","/signup");


//        return allowedPaths.stream().anyMatch(requestUri::startsWith);
        return allowedPaths.stream().anyMatch(p -> pathMatcher.match(p, requestUri));
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
//            //토큰
//            String token = authorization;
            String accessToken = tokenProvider.extractAccessToken(request).orElse(null);
            String refreshToken = tokenProvider.extractRefreshToken(request).orElse(null);

            logger.info(accessToken+":"+refreshToken);

            if (tokenRepository.findByAccessToken(accessToken).isEmpty()){
                sendUnauthorizedResponse(response,"Access is Invalid");
                return;
            }
            if (accessToken!=null&& tokenProvider.isTokenValid(accessToken)){
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request,response);
            }
        }catch (ExpiredJwtException e){
            //401에러

            sendUnauthorizedResponse(response,"401");
        }
        catch (Exception e){
            SecurityContextHolder.clearContext();
            logger.error("Authentication ERROR : ", e);
            sendUnauthorizedResponse(response, "Authentication  error :" + e.getMessage());
        }
    }

    //인가되지 않은 사용자에게 띄어줄 페이지
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }



}