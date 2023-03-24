package com.example.app.service.impl;

import com.example.app.repository.RefreshTokenRepository;
import com.example.app.repository.SaltRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import com.example.app.utils.JwtProvider;
import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.RefreshToken;
import com.example.app.utils.model.entities.Salt;
import com.example.app.utils.model.entities.User;
import com.example.app.security.Crypto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    SaltRepository saltRepository;
    RefreshTokenRepository refreshTokenRepository;
    Crypto crypto;
    JwtProvider jwtProvider;


    @Override
    public JwtResponse login(@NonNull User user) throws UserNotFoundException {
        String hashedPassword = getHash(user);
        if(!userRepository.existsUserEntityByLoginAndPassword(user.getLogin(), hashedPassword))
            throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
        String accessToken = jwtProvider.generateJwtAccessToken(user);
        String refreshToken = jwtProvider.generateJwtRefreshToken(user);
        saveRefreshToken(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse registration(@NonNull User user) throws UserAlreadyRegisteredException {
        if(userRepository.existsUserEntityByLogin(user.getLogin())) throw new UserAlreadyRegisteredException();
        String hashedPassword = doHash(user);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        String accessToken = jwtProvider.generateJwtAccessToken(user);
        String refreshToken = jwtProvider.generateJwtRefreshToken(user);
        saveRefreshToken(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    private String getHash(@NonNull User user) throws UserNotFoundException {
        Salt salts = saltRepository.getSaltByOwner(user.getLogin());
        if(salts == null) throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
        String salt1 = salts.getSalt1();
        String salt2 = salts.getSalt2();
        String hash = crypto.md5(salt1 + user.getPassword() + salt2);
        return crypto.md5(hash);
    }


    private String doHash(@NonNull User user){
        String salt1 = crypto.generateSalt(20);
        String salt2 = crypto.generateSalt(20);
        Salt salt = new Salt(salt1, salt2, user.getLogin());
        saltRepository.save(salt);
        String hash = crypto.md5(salt1 + user.getPassword() + salt2);
        return crypto.md5(hash);
    }


    private void saveRefreshToken(String login, String token){
        RefreshToken refreshToken = new RefreshToken(login, token);
        refreshTokenRepository.save(refreshToken);
    }

}
