package com.example.app.utils.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Salt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String salt1;
    private String salt2;

    private String owner;

    public Salt(String salt1, String salt2, String owner){
        this.salt1 = salt1;
        this.salt2 = salt2;
        this.owner = owner;
    }

}
