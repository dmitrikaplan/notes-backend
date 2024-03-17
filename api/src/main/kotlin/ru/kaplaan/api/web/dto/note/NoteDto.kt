package ru.kaplaan.api.web.dto.note

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import ru.kaplaan.api.web.validation.OnCreate
import ru.kaplaan.api.web.validation.OnUpdate

class NoteDto(

    var title: String,

    @field:NotBlank(message = "Text must be not blank", groups = [OnCreate::class])
    var text: String

){
    @field:NotNull(message = "Id must be not null", groups = [OnUpdate::class])
    @field:Null(message = "Id must be null", groups = [OnCreate::class])
    var id: Long? = null
}
