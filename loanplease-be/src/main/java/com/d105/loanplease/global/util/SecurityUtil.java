package com.d105.loanplease.global.util;

import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.exception.ErrorCode;
import com.d105.loanplease.global.exception.Exceptions;
import io.netty.channel.unix.Errors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class SecurityUtil {

    private static UserRepository userRepository;

    //유저 정보를 가져온다.
    public static User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return userRepository.findByEmail(((UserDetails) authentication.getPrincipal()).getUsername())
                    .orElseThrow(() -> new Exceptions(ErrorCode.MEMBER_NOT_EXIST)) ;
        }
        throw new IllegalStateException("No authenticated user available");
    }

    public static Long getCurrentUserId() {
        User userDetails = getCurrentUserDetails();
        if (userDetails != null){
            return userDetails.getUserId();
        }
        throw new IllegalStateException("UserDetails does not contain user ID information");
    }
    public static String getCurrentUserEmail(){
        User userDetails = getCurrentUserDetails();
//        if (User instanceof User) {  //
//            return ((User) userDetails).getEmail();
//        }
        if (userDetails != null){
            return userDetails.getEmail();
        }
        throw new IllegalStateException("UserDetails does not contain user ID information");
    }
}
