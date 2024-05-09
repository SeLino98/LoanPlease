package com.d105.loanplease.domain.friend.repository;

import com.d105.loanplease.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 필요한 추가 쿼리 메서드를 정의할 수 있습니다.
}

