package ru.kaplaan.authserver.domain.entity

import jakarta.persistence.*

@Entity
data class RefreshToken(
    var refreshToken: String,
    var login: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    constructor(id: Long, refreshToken: String, login: String): this(refreshToken, login){
        this.id = id
    }
}
