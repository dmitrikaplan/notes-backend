package ru.kaplaan.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated

@Entity
data class Salt(
    var salt1: String,
    var salt2: String,
    var owner: String,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long? = null

    constructor(id: Long, salt1: String, salt2: String, owner: String): this(salt1, salt2, owner){
        this.id = id
    }
}
