package com.example.app.utils;

import io.jsonwebtoken.Claims;

public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }
}
