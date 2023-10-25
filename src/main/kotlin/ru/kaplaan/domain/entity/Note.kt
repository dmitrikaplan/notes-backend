package ru.kaplaan.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.validation.annotation.Validated

@Entity
class Note(
    var title: String,
    var text: String,
    var owner: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null
}
