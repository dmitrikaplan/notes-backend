package com.example.app.utils.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String login;
    private String refreshToken;

    public RefreshToken(String login, String refreshToken){
        this.login = login;
        this.refreshToken = refreshToken;
    }

}
