package com.d105.loanplease.global.cipher;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashUtil {
    // 단방향 암호화
    // 암호화
    public static String hash(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    // 검증
    public static boolean verify(String value, String hashedValue) { //매치되는지만 확인한다.
        return BCrypt.checkpw(value, hashedValue);
    }


}
