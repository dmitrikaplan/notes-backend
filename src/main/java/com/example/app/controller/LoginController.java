package com.example.app.controller;

import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.NotValidUserException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.JwtResponse;
import com.example.app.utils.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/login")
@AllArgsConstructor
public class LoginController {

    private AuthService authService;

    @PostMapping
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
}
