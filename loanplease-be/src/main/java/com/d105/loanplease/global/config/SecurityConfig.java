package com.d105.loanplease.global.config;

import com.d105.loanplease.domain.auth.jwt.JWTFilter;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.auth.oauth.CustomSuccessHandler;
import com.d105.loanplease.domain.auth.repository.TokenRepository;
import com.d105.loanplease.domain.auth.service.CustomOAuth2UserService;
import com.d105.loanplease.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(tokenProvider, tokenRepository), UsernamePasswordAuthenticationFilter.class);

//        //oauth2
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                                .userService(customOAuth2UserService))
//                        .successHandler(customSuccessHandler)
//                );
//
//        //경로별 인가 작업
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/api/upload").permitAll()
//                        .requestMatchers("/api/server").permitAll()
//                                .requestMatchers("/api/auth/nickname/**").permitAll()
//                                .requestMatchers("/api/auth/register/").permitAll()
//                                .requestMatchers("/signup").permitAll()
//                        .anyRequest().authenticated()
////                                .anyRequest().permitAll()
//                );

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}