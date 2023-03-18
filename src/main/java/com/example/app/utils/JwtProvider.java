package com.example.app.utils;

import com.example.app.security.Crypto;
import com.example.app.utils.model.entities.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@NoArgsConstructor
public class JwtProvider {

    private SecretKey jwtAccessToken;
    private SecretKey jwtRefreshToken;
    private Crypto crypto;

    @Autowired
    public JwtProvider(Crypto crypto) {
        this.crypto = crypto;
    }

    public void setJwtAccessToken(String data) {
        String cryptoData = crypto.getIntoBase64(data);
        this.jwtAccessToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(cryptoData));
    }

    public void setJwtRefreshToken(String data) {
        String cryptoData = crypto.getIntoBase64(data);
        this.jwtRefreshToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(cryptoData));
    }


    public String generateJwtAccessToken(@NonNull UserEntity user) {
        setJwtAccessToken(user.getLogin());
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts
                .builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessToken)
                .compact();
    }


    public String generateJwtRefreshToken(@NonNull UserEntity user) {
        setJwtRefreshToken(user.getLogin());
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts
                .builder()
                .setExpiration(refreshExpiration)
                .setSubject(user.getLogin())
                .signWith(jwtRefreshToken)
                .compact();
    }


    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessToken);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshToken);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key key) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported jwt", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed jwt", e);
        } catch (SignatureException e) {
            log.error("Invalid signature", e);
        } catch (Exception e) {
            log.error("invalid token", e);
        }

        return false;
    }

    public Claims getAccessClaims(@NonNull String accessToken) {
        return getClaims(accessToken, jwtAccessToken);
    }

    public Claims getRefreshClaims(@NonNull String refreshToken) {
        return getClaims(refreshToken, jwtRefreshToken);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
