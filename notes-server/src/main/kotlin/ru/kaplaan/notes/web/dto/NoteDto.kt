package ru.kaplaan.notes.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import ru.kaplaan.domain.domain.web.validation.OnCreate
import ru.kaplaan.domain.domain.web.validation.OnUpdate

@Schema(description = "Сущность заметки")
class NoteDto(

    @Schema(description = "Заголовок заметки", example = "Планы на день")
    var title: String,

    @Schema(description = "Текст заметки", example = "1) Сделать лабораторную работу")
    @field:NotBlank(message = "Text must be not blank", groups = [OnCreate::class])
    var text: String

){
    @Schema(description = "id заметки", example = "1")
    @field:NotNull(message = "Id must be not null", groups = [OnUpdate::class])
    @field:Null(message = "Id must be null", groups = [OnCreate::class])
    var id: Long? = null
}
