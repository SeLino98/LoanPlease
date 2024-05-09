package com.d105.loanplease.domain.user.request;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpReq {
    private String nickname;
    private String email;
}
