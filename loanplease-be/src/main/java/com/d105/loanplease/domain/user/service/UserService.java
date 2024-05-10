package com.d105.loanplease.domain.user.service;

import com.d105.loanplease.domain.auth.dto.TokenResDto;
import com.d105.loanplease.domain.auth.jwt.TokenProvider;
import com.d105.loanplease.domain.auth.oauth.CustomSuccessHandler;
import com.d105.loanplease.domain.store.adapter.out.SlotRepository;
import com.d105.loanplease.domain.store.application.port.out.ItemPort;
import com.d105.loanplease.domain.store.application.port.out.LoanPort;
import com.d105.loanplease.domain.store.domain.Slot;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserItemRepository;
import com.d105.loanplease.domain.user.repository.UserLoanRepository;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.domain.user.request.UserSignUpReq;
import com.d105.loanplease.domain.user.response.UserSignUpRes;
import com.d105.loanplease.global.exception.ErrorCode;
import com.d105.loanplease.global.exception.Exceptions;
import com.d105.loanplease.global.util.BaseResponseBody;
import com.d105.loanplease.global.util.S3Image;
import com.d105.loanplease.global.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


    private final S3Image imageSave; //S3 서비스 저장
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final TokenProvider tokenProvider;
    private final HttpServletResponse response;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    private final UserItemRepository userItemRepository;
    private final UserLoanRepository userLoanRepository;

    @Value("${spring.jwt.access.header}")
    private String accessHeader;
    @Value("${spring.jwt.refresh.header}")
    private String refreshHeader;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private String saveImage(MultipartFile image, String userEmail) throws IOException {
        String mainImgUrl = "";
        if (image!=null){
            //널이 아니면 저장한다.
            mainImgUrl = imageSave.saveImageS3(image,"updated"+ userEmail + ".png", "/userProfileImage");
        }
        return mainImgUrl;
    }

    //회원 가입 기능
    @Transactional
    public UserSignUpRes  signUp(UserSignUpReq userReq, MultipartFile image) throws IOException {

        if (userRepository.findByEmail(userReq.getEmail()).isPresent()){
            //기존에 회원이 존재한다면?
            throw new Exceptions(ErrorCode.EMAIL_EXIST);
        }

        String mainImgUrl = saveImage(image,userReq.getEmail()); //S3에 저장한다.
        User newUser = User.builder()
                .nickname(userReq.getNickname())
                .email(userReq.getEmail())
                .profileImg(mainImgUrl)
                .score(0)
                .build();
        Slot slot = Slot.makeSlot(newUser);

        slotRepository.save(slot);
        userRepository.save(newUser);

        //userRepository.save가 성공하면
        //access와 refresh 토큰을 발급하고 저장한다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info(authentication.toString());

            throw new Exceptions(ErrorCode.NOT_VALID_REQUEST);

        }

        logger.info(authentication.toString());
        //엑세스 토큰을 준다.
        String accessToken = tokenProvider.createAccessJwt(authentication);
        String refreshToken = tokenProvider.createRefreshJwt(accessToken);

        //토큰을 redis에 올린다.
        tokenProvider.updateTokenRepo(newUser.getEmail(), refreshToken, accessToken);
//        response.setHeader(accessHeader, "Bearer " + accessToken);
//        response.setHeader(refreshHeader, "Bearer " + refreshToken);
//        response.addCookie(new Cookie());'accessToken', accessToken, { httpOnly: true, secure: true });
//        response.cookie('refreshToken', refreshToken, { httpOnly: true, secure: true });

        //토큰을 쿠키로 준다.
        response.addCookie(createCookie("Authorization", accessToken));
        //return refresh Token
        response.addCookie(createHttpOnlyCookie("RefreshToken",refreshToken));
        return UserSignUpRes.builder()
                .nickname(newUser.getNickname())
                .email(newUser.getEmail())
                .profileImg(newUser.getProfileImg())
                .score(newUser.getScore())
                .slotNum(newUser.getSlotNum())
                .build();

    }

    // 닉네임 중복 체크 기능
    public boolean isNicknameAvailable(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
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


    // 회원 탈퇴 기능 -> 나중 기능으로 추가
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }


    // 회원 정보 변경 기능
    @Transactional
    public void updateUserById( String nickname, MultipartFile image) throws IOException {
        try{
            //JWT를 통해 회원 아이디값 가져오기
//            String userEmail = SecurityUtil.getCurrentUserEmail();
            User getUserInfo = SecurityUtil.getCurrentUserDetails();
            String userEmail = getUserInfo.getEmail();
            Long userId = getUserInfo.getUserId();
            //S3에 사진 저장
            String mainImgUrl = saveImage(image,userEmail);
            //빌더
            // 기존 User 정보에 새 데이터 적용
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));
            user.setNickname(nickname);
            user.setProfileImg(mainImgUrl);
            //save
            // 변경된 사용자 정보 저장
            userRepository.save(user);
        } catch (IllegalStateException e) {
            // 예외를 호출자에게 전달
            throw new IllegalStateException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public void getUserInfo() {

    }
}
