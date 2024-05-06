package com.d105.loanplease.domain.auth.controller;


import com.d105.loanplease.domain.auth.entity.Token;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

//    @Value("${jwt.access.header}")
//    private String accessHeader;
//    @Value("${jwt.refresh.header}")
//    private String refreshHeader;
//    private final TokenProvider tokenProvider;
//    private final TokenRepository tokenRepository;
//    private final HttpServletRequest request;
//    private final HttpServletResponse response;
//
//    @GetMapping("/refresh")
//    public ResponseEntity<? extends BaseResponseBody> regenerateAccessToken() {
//        System.out.println(request.getHeader(refreshHeader).substring(7));
//        Token token = tokenRepository.findByRefreshToken(request.getHeader(refreshHeader).substring(7)).orElse(null);
//        System.out.println(token.getRefreshToken());
//        System.out.println("1");
//        if(token != null) {
//            System.out.println("checkpoint");
//            tokenProvider.checkRefreshTokenAndReIssueAccessToken(request, response, request.getHeader(refreshHeader).substring(7));
//            TokenResDto tokenResDto = TokenResDto.builder()
//                    .grantType("Bearer")
//                    .accessToken(tokenRepository.findById(token.getId()).get().getAccessToken())
//                    .refreshToken(tokenRepository.findById(token.getId()).get().getRefreshToken())
//                    .build();
//            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, tokenResDto));
//        }
//        System.out.println("2");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.error(ErrorCode.NOT_VALID_REQUEST.getErrorCode(),ErrorCode.NOT_VALID_REQUEST.getMessage()));
//    }

}
