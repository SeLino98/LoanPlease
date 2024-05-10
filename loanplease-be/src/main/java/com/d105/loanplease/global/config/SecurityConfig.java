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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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

        //CORS
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
                    configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                })));

        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(tokenProvider, tokenRepository), UsernamePasswordAuthenticationFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/upload").permitAll()
                        .requestMatchers("/api/server").permitAll()
                                .requestMatchers("/api/auth/nickname/**").permitAll()
                                .requestMatchers("/api/auth/register/").permitAll()
                                .requestMatchers("/signup").permitAll()
                        .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                );

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(Arrays.asList("*"));
        cors.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        cors.setAllowedHeaders(Arrays.asList("*"));
        cors.addExposedHeader("Authorization");
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);  // 모든 경로에 대해 CORS 설정 적용

        return source;
    }



}