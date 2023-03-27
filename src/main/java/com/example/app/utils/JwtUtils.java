package com.example.app.utils;

import com.example.app.utils.security.JwtAuthentication;
import io.jsonwebtoken.Claims;

public class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }
}
