package com.example.app.service;

import com.example.app.utils.exceptions.*;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    JwtResponse login(@NonNull User user) throws UserNotFoundException, NotValidUserException;

    void registration(@NonNull User user) throws UserAlreadyRegisteredException, NotValidUserException, MessagingException;

    void activateAccount(String code) throws NotFoundUserByActivationCode;


    void passwordRecovery(User user) throws NotValidEmailException, UserNotFoundException, MessagingException;
}
