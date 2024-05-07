package com.d105.loanplease.domain.game.serviceImpl;

import com.d105.loanplease.domain.game.dto.*;
import com.d105.loanplease.domain.game.response.GameInfoResponse;
import com.d105.loanplease.domain.game.response.ResultResponse;
import com.d105.loanplease.domain.game.response.ScoreResponse;
import com.d105.loanplease.domain.game.service.GameService;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<GameInfoResponse> getGameInfo() {

        // 랜덤으로 값을 생성한다.
        // TODO::: 랜덤 범위 찾기
        FinancialInfo financialInfo = new FinancialInfo(2500.0, "수입 유형", "직업 유형");
        NonFinancialInfo nonFinancialInfo = new NonFinancialInfo("없음", "없음", 2, "대학 졸업", "4", "아파트", 250, 3);

        // Fast API에 결과를 전송한다.
        int credit = 1;


        // 에러 처리



        // 데이터를 보낸다.
        Loan loan = new Loan(5.5, 3, "부동산 구매");
        CustomerInfo customerInfo = new CustomerInfo("나싸피", 26, "남성", "", "대출해주세요", new ArrayList<>());

        GameInfo gameInfo = new GameInfo(loan, customerInfo, financialInfo, nonFinancialInfo, credit);
        GameInfoResponse response = GameInfoResponse.createGameInfoResponse(HttpStatus.OK.value(), "게임 정보를 성공적으로 받아왔습니다.", gameInfo);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScoreResponse> gainScore(int mode) {
        switch (mode){
            case 0:


                break;
            case 1:


                break;
        }

        Score score = new Score(500, "감사합니다!", "적합한 대출을 추천했습니다");
        ScoreResponse response = ScoreResponse.createScoreResponse(HttpStatus.OK.value(), "점수를 획득했습니다.", score);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResultResponse> saveScore(int score) {
        // User의 정보를 불러옵니다.
        // TODO ::: 접속 중인 USER의 ID를 가져오는 작업 필요.
        long userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        // 점수가 최고 점수라면 갱신합니다.
        if(user.getScore() > score){
            user.setScore(score);
        }

        // User의 포인트를 계산합니다.StatisticsErrorHandler.java
        // todo::: 점수와 비례하는 포인트 계산 필요
        int point = 1000;
        user.setPoint(user.getPoint()+point);

        userRepository.save(user);

        ResultResponse response = ResultResponse.createResultResponse(HttpStatus.OK.value(), "점수를 저장했습니다.", point);
        return ResponseEntity.ok(response);
    }
}
