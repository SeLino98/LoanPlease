package com.d105.loanplease.domain.user.repository;

import com.d105.loanplease.domain.user.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
}
