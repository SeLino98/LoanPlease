package com.d105.loanplease.domain.game.controller;

import com.d105.loanplease.domain.game.response.GameInfoResponse;
import com.d105.loanplease.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/score/{mode}")
    public ResponseEntity<GameInfoResponse> gainScore(@PathVariable int mode){
        return gameService.gainScore(mode);
    };

    @PutMapping("/score")
    public ResponseEntity<GameInfoResponse> saveScore(int score){
        return gameService.saveScore(score);
    };
}
