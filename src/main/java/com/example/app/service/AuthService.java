package com.example.app.service;

import com.example.app.utils.exceptions.*;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    JwtResponse login(@NonNull User user) throws UserNotFoundException, NotValidUserException;

    void registration(@NonNull User user) throws UserAlreadyRegisteredException,  NotValidUserException;

    void activateAccount(String code) throws NotFoundUserByActivationCode;

}
