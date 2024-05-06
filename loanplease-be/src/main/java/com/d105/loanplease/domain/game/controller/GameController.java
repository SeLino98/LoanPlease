package com.d105.loanplease.domain.game.controller;

import com.d105.loanplease.domain.game.response.GameInfoResponse;
import com.d105.loanplease.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    @GetMapping("/loanrequest")
    public ResponseEntity<GameInfoResponse> getGameInfo(){
        return gameService.getGameInfo();
    };
}
