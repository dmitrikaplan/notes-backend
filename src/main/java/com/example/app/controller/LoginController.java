package com.example.app.controller;

import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/login")
@AllArgsConstructor
public class LoginController {

    AuthService authService;

    @PostMapping
    public ResponseEntity<String> login(
            @RequestBody UserEntity user
    ) {
        try{
            authService.loginUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно вошел в систему");
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким логином и паролем не найден");
        }

    }
}
