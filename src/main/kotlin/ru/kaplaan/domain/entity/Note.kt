package ru.kaplaan.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.validation.annotation.Validated

@Entity
class Note(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null
    var title: String? = null
    var text: String? = null
    var owner: String? = null

    constructor(id: Long, title: String, text: String, owner: String) : this() {
        this.id = id
        this.title = title
        this.text = text
        this.owner = owner
    }

    constructor(title: String, text: String, owner: String) : this() {
        this.title = title
        this.text = text
        this.owner = owner
    }
}
