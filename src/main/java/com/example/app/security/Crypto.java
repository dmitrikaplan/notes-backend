package com.example.app.security;

import com.example.app.utils.model.entities.User;
import io.jsonwebtoken.io.Encoders;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public String generateSalt(int saltSize) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < saltSize; i++) {
            sb.append(random.nextInt(26) + 'a');
        }
        return sb.toString();

    }

    public String generateKeyForJwt(@NonNull User user){
        String salt1 = generateSalt(120);
        String salt2 = generateSalt(120);
        return md5(md5(salt1 + user.getPassword()) + user.getLogin() + salt2);
    }

    public String getIntoBase64(@NonNull User user) {
        String salt1 = generateSalt(256);
        String salt2 = generateSalt(256);
        String secret = md5(salt2 + user.getLogin() + md5(salt1 + user.getPassword()));
        return Encoders.BASE64.encode(secret.getBytes());
    }
}
