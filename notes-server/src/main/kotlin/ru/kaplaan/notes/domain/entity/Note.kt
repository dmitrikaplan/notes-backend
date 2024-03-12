package ru.kaplaan.notes.domain.entity

import jakarta.persistence.*

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
