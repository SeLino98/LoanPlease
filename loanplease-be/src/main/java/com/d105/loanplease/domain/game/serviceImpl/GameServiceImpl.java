package com.d105.loanplease.domain.game.serviceImpl;

import com.d105.loanplease.domain.game.Fields.*;
import com.d105.loanplease.domain.game.dto.*;
import com.d105.loanplease.global.util.BaseResponse;
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
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<GameInfoResponse> getGameInfo() {

        // 랜덤으로 값을 생성한다.
        // Random 객체 생성
        Random random = new Random();
        int randomIndex;
        int min;    // 최소값
        int max;  // 최대값

        // family size : 1.0 ~ 3.0
        min = 1;
        max = 3;
        int familySize = random.nextInt((max-min)+1)+min;

        // child num : 0~7
        min = 0;
        max = 7;
        int childNum = random.nextInt((max - min)+1) + min;

        // family_type
        FamilyType[] familyTypes = FamilyType.values();
        randomIndex = random.nextInt(familyTypes.length);
        FamilyType familyType = familyTypes[randomIndex];

        // income total : 27000.0 ~ 1575000
        min = 30000;
        max = 1575000;
        int incomeTotal = random.nextInt((max-min)+1)+min;


        // DAYS_BIRTH 7705 ~ 25152
        min = 7705;
        max = 25152;
        int daysBirth = random.nextInt((max-min)+1)+min;

        // DAYS_EMPLOYED 0 ~ 8000
        min = 0;
        max = 8000;
        int daysEmployed = random.nextInt((max-min)+1)+min;

        // begin_month : 0 ~ 60
        min = 0;
        max = 60;
        int beginMonth = random.nextInt((max-min)+1)+min;

        // income_type
        IncomeType[] incomeTypes = IncomeType.values();
        randomIndex = random.nextInt(incomeTypes.length);
        IncomeType incomeType = incomeTypes[randomIndex];

        // edu_type
        EduType[] eduTypes = EduType.values();
        randomIndex = random.nextInt(eduTypes.length);
        EduType eduType = eduTypes[randomIndex];

        // house_type
        HouseType[] houseTypes = HouseType.values();
        randomIndex = random.nextInt(houseTypes.length);
        HouseType houseType = houseTypes[randomIndex];

        // occyp_type
        OccypType[] occypTypes = OccypType.values();
        randomIndex = random.nextInt(occypTypes.length);
        OccypType occypType = occypTypes[randomIndex];


        // YN 여부
        YN[] yns = YN.values();
        randomIndex = random.nextInt(yns.length);
        YN car = yns[randomIndex];
        randomIndex = random.nextInt(yns.length);
        YN reality = yns[randomIndex];

        FinancialInfo financialInfo = new FinancialInfo(incomeTotal/100, incomeType.getKoreanName(), occypType.getKoreanName());
        NonFinancialInfo nonFinancialInfo = new NonFinancialInfo(car.getKoreanName(), reality.getKoreanName(), childNum, eduType.getKoreanName(), familyType.getKoreanName(), houseType.getKoreanName(), daysEmployed, familySize);

        // 영문명을 활용하여
        // Fast API에 결과를 전송한다.
        int credit = 1;


        // 에러 처리



        // 데이터를 보낸다.
        LoanRequest loanRequest = new LoanRequest(5.5, 3);  // 랜덤 작업 필요
        String purpose = "부동산 구매 목적";   // 랜덤 작업 필요

        int age = daysBirth/360;
        String[] genders = new String[]{"남성", "여성"};
        randomIndex = random.nextInt(genders.length);
        String gender = genders[randomIndex];

        int picNumber = selectPicNumber(age, gender);

        CustomerInfo customerInfo = new CustomerInfo("나싸피", age, gender, picNumber, purpose+"으로 대출해주세요!", new ArrayList<>());

        GameInfo gameInfo = new GameInfo(loanRequest, customerInfo, financialInfo, nonFinancialInfo, credit);
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
        BaseResponse<Score> response1 = BaseResponse.createResponse(HttpStatus.OK.value(), "어ㅏㅣㅇ", score);

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
        BaseResponse<Integer> response2 = BaseResponse.createResponse(HttpStatus.OK.value(), "점수 저장", point);

        return ResponseEntity.ok(response);
    }

    private int selectPicNumber(int age, String gender){
        int generation = age/10;
        int min; int max;
        Random random = new Random();
        int[] womanPictures = new int[]{1,2,3,4,5,6,7,8, 9};
        int[] manPictures = new int[]{11,12,13,14,15,16,17,18};

        switch (gender){
            case "남성":
                if(generation <3) return manPictures[0];
                else if(generation <4){
                    min = 1; max = 2;
                    return manPictures[random.nextInt(max-min+1)+min];
                }else if (generation<5)
                    return manPictures[3];
                else if(generation<6) return manPictures[4];
                else{
                    min=5; max=7;
                    return manPictures[random.nextInt(max-min+1)+min];
                }
            case "여성":
                if(generation <3){
                    min = 0; max = 3;
                    return womanPictures[random.nextInt(max-min+1)];
                }else if(generation <4){
                    min = 4; max = 5;
                    return womanPictures[random.nextInt(max-min+1)+min];
                }else if (generation<5){
                    return womanPictures[6];
                }else if(generation<6) return womanPictures[7];
                else return womanPictures[8];
        }
        return 1;
    }
}
