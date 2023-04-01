package com.example.app.controller;

import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.*;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;


    @PostMapping("registration")
    public ResponseEntity<String> registration(@RequestBody User user){
        try{
            authService.registration(user);
            return ResponseEntity.status(HttpStatus.OK).body("Код подтверждения отправлен вам на почту");
        }
        catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь уже зарегистрирован");
        } catch (NotValidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректные(й) логин и/или пароль, и/или почта");
        }
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody User user
    ) {
        try{
            JwtResponse jwtResponse = authService.login(user);
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        }
        catch (UserNotFoundException e){
            JwtResponse jwtResponse = new JwtResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jwtResponse);
        } catch (NotValidUserException e) {
            JwtResponse jwtResponse = new JwtResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtResponse);
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
