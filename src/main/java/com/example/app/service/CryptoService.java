package com.example.app.service;

import com.example.app.repository.SaltRepository;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.entities.Salt;
import com.example.app.utils.model.entities.User;
import io.jsonwebtoken.io.Encoders;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Service
public class CryptoService {

    SaltRepository saltRepository;

    public CryptoService(SaltRepository saltRepository){
        this.saltRepository = saltRepository;
    }

    public String getHash(@NonNull User user) throws UserNotFoundException {
        Salt salts = saltRepository.getSaltByOwner(user.getLogin());
        if (salts == null) throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
        String salt1 = salts.getSalt1();
        String salt2 = salts.getSalt2();
        String hash = md5(salt1 + user.getPassword() + salt2);
        return md5(hash);
    }


    public String doHash(@NonNull User user) {
        String salt1 = generateSalt(20);
        String salt2 = generateSalt(20);
        Salt salt = new Salt(salt1, salt2, user.getLogin());
        saltRepository.save(salt);
        String hash = md5(salt1 + user.getPassword() + salt2);
        return md5(hash);
    }


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
        String salt3 = generateSalt(30);
        String secret = md5(salt2 + user.getLogin() + md5(salt1 + user.getPassword())) + salt3;
        return Encoders.BASE64.encode(secret.getBytes());
    }
}
