package com.d105.loanplease.domain.user.controller;

import com.d105.loanplease.domain.auth.dto.TokenResDto;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.auth.repository.TokenRepository;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.request.UserSignUpReq;
import com.d105.loanplease.domain.user.response.UserSignUpRes;
import com.d105.loanplease.domain.user.service.UserService;
import com.d105.loanplease.global.exception.ErrorCode;
import com.d105.loanplease.global.exception.Exceptions;
import com.d105.loanplease.global.util.BaseResponseBody;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {


    //회원가입
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @CrossOrigin(origins = "https://loanplease.kr:443")
    @PostMapping("/api/auth/register")
    public ResponseEntity<BaseResponseBody> registerUser(
            @RequestParam("email") String email,
            @RequestParam("nickname") String nickname,
            @RequestParam("profileImage") String profileImage
    ) throws Exception {
        UserSignUpReq userReq = UserSignUpReq.builder().email(email).nickname(nickname).profileImage(profileImage).build();

        try {
            UserSignUpRes userSignUpRes = userService.signUp(userReq);
            // 여기서 헤더 설정은 이미 서비스에서 처리됨
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of("200", userSignUpRes));
        } catch (Exceptions e) {
            if (e.getErrorCode() == ErrorCode.EMAIL_EXIST) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.error(ErrorCode.EMAIL_EXIST.getErrorCode(), "Email already exists."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.error(ErrorCode.NOT_VALID_REQUEST.getErrorCode(), "Invalid request."));
            }
        }
    }

    @PutMapping("/api/auth/")
    public ResponseEntity<BaseResponseBody<Void>> updateUser
            (@RequestParam String nickname,
             @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        userService.updateUserById(nickname, profileImage);
        return ResponseEntity.ok(BaseResponseBody.of("200", null));
    }

    //닉네임이 사용 중인지 확인한다.
    @GetMapping("/api/auth/nickname/{nickname}")
    public ResponseEntity<BaseResponseBody<Boolean>> checkNicknameAvailability(
            @PathVariable("nickname") String nickname) {
        log.info("ASDas");log.info("ASDas");log.info("ASDas");log.info("ASDas");
        boolean isAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(BaseResponseBody.of("200", isAvailable));
    }//end


    @DeleteMapping("/api/auth/") //난중에 구현..
    public ResponseEntity<BaseResponseBody<Void>> deleteUser() {
//        userService.deleteUserById();
        return ResponseEntity.ok(BaseResponseBody.of("200", null));
    }

}




    //신규 유저인지는 구글로부터 정보 받고 거기서 처리한다.
//    @GetMapping("/email/{email}")
//    public ResponseEntity<BaseResponseBody<User>> getUserByEmail(@PathVariable String email) {
//        User user = userService.findUserByEmail(email);
//        return ResponseEntity.ok(BaseResponseBody.of("200", user));
//    }



