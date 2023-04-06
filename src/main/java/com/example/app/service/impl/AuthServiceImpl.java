package com.example.app.service.impl;

import com.example.app.repository.RefreshTokenRepository;
import com.example.app.repository.SaltRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import com.example.app.service.CryptoService;
import com.example.app.service.EmailService;
import com.example.app.service.ValidationService;
import com.example.app.utils.JwtProvider;
import com.example.app.utils.exceptions.*;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.RefreshToken;
import com.example.app.utils.model.entities.User;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private EmailService emailService;
    private CryptoService cryptoService;
    private JwtProvider jwtProvider;
    private ValidationService validationService;

    public AuthServiceImpl(
            UserRepository userRepository,
            SaltRepository saltRepository,
            RefreshTokenRepository refreshTokenRepository,
            EmailService emailService,
            CryptoService cryptoService,
            JwtProvider jwtProvider,
            ValidationService validationService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.cryptoService = cryptoService;
        this.jwtProvider = jwtProvider;
        this.validationService = validationService;
    }


    @Override
    public JwtResponse login(@NonNull User user) throws UserNotFoundException, NotValidUserException {
        checkLogin(user);
        String accessToken = jwtProvider.generateJwtAccessToken(user);
        String refreshToken = jwtProvider.generateJwtRefreshToken(user);
        saveRefreshToken(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public void registration(@NonNull User user) throws UserAlreadyRegisteredException, NotValidUserException, MessagingException {
        checkRegistration(user);


        final String hashedPassword = cryptoService.doHash(user);
        final String activationCode = UUID.randomUUID().toString().replace("-", "");

        user.setPassword(hashedPassword);
        user.setActivationCode(activationCode);
        emailService.activateUserByEmail(user.getEmail(), user.getLogin(), activationCode);
        userRepository.save(user);
    }

    @Override
    public void activateAccount(String code) throws NotFoundUserByActivationCode {
        final User user = userRepository.getUserByActivationCode(code);
        if (user == null) throw new NotFoundUserByActivationCode();
        user.setActivationCode(null);
        user.setActivated(true);
        userRepository.save(user);
    }

    @Override
    public void passwordRecovery(User user) throws NotValidEmailException, UserNotFoundException, MessagingException {
        User u = getUserByLoginOrEmail(user);
        if(!validationService.validateEmail(u.getEmail())) throw new NotValidEmailException();

        String activationCode = UUID.randomUUID().toString().replace("-", "");
        user.setActivationCode(activationCode);

        emailService.recoveryPasswordByEmail(u.getEmail(),u.getLogin(), activationCode);
        userRepository.save(user);
    }

    private User getUserByLoginOrEmail(User user) throws UserNotFoundException {
        if(user.getLogin() == null) {
            User u = userRepository.getUserByEmail(user.getEmail());
            if(u == null) throw new UserNotFoundException("Пользователь с такой почтой не найден");
            user.setLogin(u.getLogin());
        } else{
            User u = userRepository.getUserByLogin(user.getLogin());
            if(u == null) throw new UserNotFoundException("Пользователь с таким логином не найден");
            user.setEmail(u.getEmail());
        }
        return user;
    }



    private void saveRefreshToken(String login, String token) {
        RefreshToken refreshToken = new RefreshToken(login, token);
        refreshTokenRepository.save(refreshToken);
    }

    private void checkLogin(User user) throws NotValidUserException, UserNotFoundException {
        validationService.validateLogin(user);
        User u = getUserByLoginOrEmail(user);
        String hashedPassword = cryptoService.getHash(user);
        if(!userRepository.existsUserByLoginAndPasswordAndEmailAndActivated
                (u.getLogin(), hashedPassword, u.getEmail(),true)) throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
    }

    private void checkRegistration(User user) throws NotValidUserException, UserAlreadyRegisteredException {
        validationService.validateRegistration(user);
        if(userRepository.existsUserByLoginOrEmailAndActivated(user.getLogin(), user.getEmail(), true))
            throw new UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует");

    }

}
