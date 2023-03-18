package com.example.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Component
@Slf4j
public class Crypto {

    public String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md.digest(password.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error("не найден алгоритм шифрования");
            return "";
        }

    }

    public String generateSalt() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(random.nextInt(26) + 'a');
        }
        return sb.toString();

    }

    public String getIntoBase64(String message) {
        return new String(Base64.getDecoder().decode(message));
    }
}
