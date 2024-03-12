package ru.kaplaan.authserver.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/auth")
@Tag(name = "View Controller", description = "Контроллер представления")
class ViewController {
    @GetMapping("{activationCode}")
    @Operation(
        summary = "Представление восстановления пароля"
    )
    fun passwordRecovery(
        @PathVariable("activationCode")
        @Parameter(description = "code восстановления пароля")
        activationCode: String,
        model: Model
    ): String {
        model.addAttribute("code", activationCode)
        return "recovery"
    }
}
