package com.example.app.service;

import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    JwtResponse login(@NonNull User user) throws UserNotFoundException;

    JwtResponse registration(@NonNull User user) throws UserAlreadyRegisteredException;

}
