package com.d105.loanplease.domain.auth.controller;


import com.d105.loanplease.domain.auth.dto.TokenResDto;
import com.d105.loanplease.domain.auth.entity.Token;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.auth.repository.TokenRepository;
import com.d105.loanplease.global.exception.ErrorCode;
import com.d105.loanplease.global.util.BaseResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    @Value("${spring.jwt.access.header}")
    private String accessHeader;
    @Value("${spring.jwt.refresh.header}")
    private String refreshHeader;


    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @GetMapping("/refresh")
    public ResponseEntity<? extends BaseResponseBody> regenerateAccessToken() {
        System.out.println("ASDFDSFASDFSDF");
        System.out.println(request.getHeader(refreshHeader).substring(7));
        Token token = tokenRepository.findByRefreshToken(request.getHeader(refreshHeader).substring(7)).orElse(null);
        System.out.println(token.getRefreshToken());
        System.out.println("1");
        if(token != null) {
            System.out.println("checkpoint");
            tokenProvider.checkRefreshTokenAndReIssueAccessToken(request, response, request.getHeader(refreshHeader).substring(7));
            TokenResDto tokenResDto = TokenResDto.builder()
                    .grantType("Bearer")
                    .accessToken(tokenRepository.findById(token.getId()).get().getAccessToken())
                    .refreshToken(tokenRepository.findById(token.getId()).get().getRefreshToken())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of("200", tokenResDto));
        }
        //머지함
        System.out.println("2");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.error(ErrorCode.NOT_VALID_REQUEST.getErrorCode(),ErrorCode.NOT_VALID_REQUEST.getMessage()));
    }

}
