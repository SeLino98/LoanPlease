package com.d105.loanplease.domain.auth.repository;

import com.d105.loanplease.domain.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String > {
    Optional<Token> findById(String email);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);
}