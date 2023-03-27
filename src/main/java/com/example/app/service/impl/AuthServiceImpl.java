package com.example.app.service.impl;

import com.example.app.repository.RefreshTokenRepository;
import com.example.app.repository.SaltRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import com.example.app.service.MailSender;
import com.example.app.utils.JwtProvider;
import com.example.app.utils.UtilsForEmails;
import com.example.app.utils.exceptions.NotFoundUserByActivationCode;
import com.example.app.utils.exceptions.NotValidLoginException;
import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.RefreshToken;
import com.example.app.utils.model.entities.Salt;
import com.example.app.utils.model.entities.User;
import com.example.app.utils.security.Crypto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private SaltRepository saltRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private MailSender mailSender;
    private Crypto crypto;
    private JwtProvider jwtProvider;
    @Value("${kittynotes.host}")
    private String host;

    public AuthServiceImpl(
            UserRepository userRepository,
            SaltRepository saltRepository,
            RefreshTokenRepository refreshTokenRepository,
            MailSender mailSender,
            Crypto crypto,
            JwtProvider jwtProvider
    ){
        this.userRepository = userRepository;
        this.saltRepository = saltRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mailSender = mailSender;
        this.crypto = crypto;
        this.jwtProvider = jwtProvider;
    }



    @Override
    public JwtResponse login(@NonNull User user) throws UserNotFoundException {
        String hashedPassword = getHash(user);
        if(!userRepository.existsUserEntityByLoginAndPasswordAndActivated(user.getLogin(), hashedPassword, true))
            throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
        String accessToken = jwtProvider.generateJwtAccessToken(user);
        String refreshToken = jwtProvider.generateJwtRefreshToken(user);
        saveRefreshToken(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public void registration(@NonNull User user) throws UserAlreadyRegisteredException, NotValidLoginException {
        final User u = userRepository.getUserByEmail(user.getEmail());
        if(u != null && u.isActivated()) throw new UserAlreadyRegisteredException();
        if(user.getEmail().isEmpty()) throw new NotValidLoginException();

        final String hashedPassword = doHash(user);
        final String activationCode = UUID.randomUUID().toString().replace("-", "");
        final String message = UtilsForEmails.getMessageForActivation(user.getEmail(), host, activationCode);

        user.setPassword(hashedPassword);
        user.setActivationCode(activationCode);
        mailSender.send(user.getEmail(), UtilsForEmails.getSubjectForActivation(), message);
        userRepository.save(user);
    }

    @Override
    public void activateAccount(String code) throws NotFoundUserByActivationCode {
        final User user = userRepository.getUserByActivationCode(code);
        if(user == null) throw new NotFoundUserByActivationCode();
        user.setActivationCode(null);
        user.setActivated(true);
        userRepository.save(user);
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
