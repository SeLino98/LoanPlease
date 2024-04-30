package com.d105.loanplease.domain.user.repository;

import com.d105.loanplease.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);
}
