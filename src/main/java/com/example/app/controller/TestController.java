package com.example.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@AllArgsConstructor
public class TestController {

    @GetMapping
    public ResponseEntity<String> test(){
        String name = "Dima";
        return ResponseEntity.status(HttpStatus.OK).body("Привет, " + name);
    }
}
