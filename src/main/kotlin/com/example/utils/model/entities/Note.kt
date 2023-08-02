package com.example.utils.model.entities

import jakarta.persistence.*

@Entity
class Note() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long? = null
    private var title: String? = null
    private var text: String? = null
    private var owner: String? = null

    constructor(
        title: String? = null,
        text: String? = null,
        owner: String? = null) : this() {
        this.title = title
        this.text = text
        this.owner = owner
    }

    fun getId() =
        id

    fun setId(id: Long){
        this.id = id
    }

    fun getTitle() =
        title

    fun setTitle(title: String){
        this.title = title
    }

    fun getText() =
        text

    fun setText(text: String){
        this.text = text
    }

    fun getOwner() =
        owner

    fun setOwner(owner: String){
        this.owner = owner
    }
}
