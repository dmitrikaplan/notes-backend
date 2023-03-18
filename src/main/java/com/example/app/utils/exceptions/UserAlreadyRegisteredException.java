package com.example.app.utils.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyRegisteredException extends Exception{

    public UserAlreadyRegisteredException(String message){
        super(message);
    }
}
