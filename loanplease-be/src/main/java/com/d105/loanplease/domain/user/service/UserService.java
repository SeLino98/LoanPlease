package com.d105.loanplease.domain.user.service;

import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // 회원 탈퇴 기능
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    // 회원 정보 변경 기능
    @Transactional
    public void updateUserById(Long userId, String nickname, String profileImg) {
        userRepository.updateUserById(userId, nickname, profileImg);
    }

    // 닉네임 중복 체크 기능
    public boolean isNicknameAvailable(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }
}
