package com.example.app.controller;

import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.model.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reg")
@AllArgsConstructor
public class RegistrationController {

    AuthService authService;

    @PostMapping
    public ResponseEntity<String> registration(@RequestBody UserEntity user){
        try{
            authService.registration(user);
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно зарегистрирован");
        }
        catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким логином уже зарегистрирован");
        }
    }


}
