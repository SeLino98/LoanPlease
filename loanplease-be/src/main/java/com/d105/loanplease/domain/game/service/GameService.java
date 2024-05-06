package com.d105.loanplease.domain.game.service;

import com.d105.loanplease.domain.game.dto.GameInfo;
import com.d105.loanplease.domain.game.response.GameInfoResponse;
import com.d105.loanplease.domain.game.response.ResultResponse;
import com.d105.loanplease.domain.game.response.ScoreResponse;
import org.springframework.http.ResponseEntity;

public interface GameService {
    ResponseEntity<GameInfoResponse> getGameInfo();

    ResponseEntity<ScoreResponse> gainScore(int mode);

    ResponseEntity<ResultResponse> saveScore(int score);
}
