package com.d105.loanplease.domain.game.service;

import com.d105.loanplease.domain.game.dto.GameInfo;
import com.d105.loanplease.domain.game.response.GameInfoResponse;
import org.springframework.http.ResponseEntity;

public interface GameService {
    ResponseEntity<GameInfoResponse> getGameInfo();

    ResponseEntity<GameInfoResponse> gainScore(int mode);

    ResponseEntity<GameInfoResponse> saveScore(int score);
}
