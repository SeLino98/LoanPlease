package com.d105.loanplease.service;

import com.d105.loanplease.oauth.CustomOAuth2User;
import com.d105.loanplease.oauth.GoogleResponse;
import com.d105.loanplease.oauth.OAuth2Response;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            log.info("구글로그인 받아옴 : {}", oAuth2User.getAttributes());
        }
        else {

            return null;
        }


        //기존 유저인지 아닌지 찾기
//        User existUser = userRepository.findByEmail(oAuth2Response.getEmail());

        User userDTO = new User();
        userDTO.setEmail(oAuth2Response.getEmail());
        userDTO.setRole("ROLE_USER");

//        if(existUser == null){
//            userRepository.save(userDTO);
//        }

        return new CustomOAuth2User(userDTO);


    }
}