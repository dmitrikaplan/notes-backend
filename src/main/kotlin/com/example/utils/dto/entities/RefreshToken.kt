package com.example.utils.dto.entities

import jakarta.persistence.*

@Entity
class RefreshToken() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long? = null
    private var refreshToken: String? = null
    private var login: String? = null

    constructor(refreshToken: String, login: String): this() {
        this.refreshToken = refreshToken
        this.login = login
    }

    fun getId() =
        id

    fun setId(id: Long){
        this.id = id
    }

    fun getRefreshToken() =
        refreshToken

    fun setRefreshToken(refreshToken: String){
        this.refreshToken = refreshToken
    }

    fun getLogin() =
        login

    fun setLogin(login: String){
        this.login = login
    }
}
