package com.example.app.controller;

import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.NotFoundUserByActivationCode;
import com.example.app.utils.exceptions.NotValidLoginException;
import com.example.app.utils.exceptions.NotValidUserException;
import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class RegistrationController {

    private AuthService authService;


    @PostMapping("registration")
    public ResponseEntity<String> registration(@RequestBody User user){
        try{
            authService.registration(user);
            return ResponseEntity.status(HttpStatus.OK).body("Успешно");
        }
        catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь уже зарегистрирован");
        } catch (NotValidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректные(й) логин и/или пароль, и/или почта");
        }
    }

    @GetMapping("activate/{code}")
    public String activateAccount(@PathVariable("code") String code){
        try {
            authService.activateAccount(code);
            return "Аккаунт успешно активирован";
        } catch (NotFoundUserByActivationCode e) {
            return "Ошибка активации аккаунта, повторите попытку позже";
        }
    }


}
