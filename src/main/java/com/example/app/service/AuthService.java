package com.example.app.service;

import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.entities.UserEntity;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    void loginUser(@NonNull UserEntity user) throws UserNotFoundException;

    void registration(@NonNull UserEntity user) throws UserAlreadyRegisteredException;

}
