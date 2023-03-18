package com.example.app.utils.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
