package ru.kaplaan.web.dto.note

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import ru.kaplaan.web.validation.OnCreate
import ru.kaplaan.web.validation.OnUpdate

@Schema(description = "Сущность заметки")
class NoteDto(){
    @Schema(description = "id заметки", example = "1")
    @NotNull(message = "Id must be not null", groups = [OnUpdate::class])
    @Null(message = "Id must be null", groups = [OnCreate::class])
    private var id: Long? = null

    @Schema(description = "Заголовок заметки", example = "Планы на день")
    private var title: String? = null

    @Schema(description = "Текст заметки", example = "1) Сделать лабораторную работу\n2)Поспать")
    @NotBlank(message = "Text must be not blank", groups = [OnCreate::class])
    private var text: String? = null

    @Schema(description = "Логин владельца заметки", example = "john-doe")
    @NotBlank(message = "Owner must be not blank", groups = [OnCreate::class])
    private var owner: String? = null

    constructor(
        title: String? = null,
        text: String? = null,
        owner: String? = null
    ) : this() {
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
