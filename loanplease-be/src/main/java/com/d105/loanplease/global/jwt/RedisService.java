package com.d105.loanplease.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 키-벨류 설정
    public void setValues(String key, String value) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    // 키값으로 벨류 가져오기
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    // 패턴에 맞는 키 검색
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // 키-벨류 삭제
    public void delValues(String key) {
        redisTemplate.delete(key);
    }
}

// RedisSerivce
//@Service
//@RequiredArgsConstructor
//public class RedisService {
//
//    private final RedisTemplate redisTemplate;
//
//
//    // 키-벨류 설정
//    public void setValues(String token, String email){
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        values.set(token, email);
////        values.set(token, email, Duration.ofMinutes(3));  // 3분 뒤 메모리에서 삭제된다.
//    }
//
//    // 키값으로 벨류 가져오기
//    public String getValues(String token){
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        return values.get(token);
//    }
//    // 특정 패턴의 키 검색
//    public Set<String> getKeys(String pattern) {
//        return redisTemplate.keys(pattern);
//    }
//
//
//    // 키-벨류 삭제
//    public void delValues(String token) {
//        redisTemplate.delete(token.substring(7));
//    }
//}
