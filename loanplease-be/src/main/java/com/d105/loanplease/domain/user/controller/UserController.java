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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    //회원가입
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseBody> registerUser(
            @RequestParam("email") String email,
            @RequestParam("nickname") String nickname,
            @RequestParam("image") MultipartFile image
            )
            throws Exception{
//        UserSignUpRes memberId = userService.signUp(userReq);
//        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, memberId));
        UserSignUpReq userReq = UserSignUpReq.builder().email(email).nickname(nickname).build();
        //new UserSignUpReq(nickname, email);
        try {
            UserSignUpRes userSignUpRes = userService.signUp(userReq,image);
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


    //신규 유저인지는 구글로부터 정보 받고 거기서 처리한다.
    @GetMapping("/email/{email}")
    public ResponseEntity<BaseResponseBody<User>> getUserByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(BaseResponseBody.of("200", user));
    }

    //
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponseBody<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok(BaseResponseBody.of("200", null));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponseBody<Void>> updateUser(@PathVariable Long userId, @RequestParam String nickname, @RequestParam(required = false) String profileImg) {
        userService.updateUserById(userId, nickname, profileImg);
        return ResponseEntity.ok(BaseResponseBody.of("200", null));
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<BaseResponseBody<Boolean>> checkNicknameAvailability(@PathVariable String nickname) {
        boolean isAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(BaseResponseBody.of("200", isAvailable));
    }
}
