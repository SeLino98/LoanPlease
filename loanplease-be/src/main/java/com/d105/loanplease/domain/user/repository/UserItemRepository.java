package com.d105.loanplease.domain.user.repository;

import com.d105.loanplease.domain.user.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
}
