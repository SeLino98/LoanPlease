package com.d105.loanplease.global.util;

import com.d105.loanplease.domain.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    //유저 정보를 가져온다.



    public static User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
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
