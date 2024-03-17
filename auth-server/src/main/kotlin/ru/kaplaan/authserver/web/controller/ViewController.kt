package ru.kaplaan.authserver.web.controller


import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/auth/view")
class ViewController {
    @GetMapping("{activationCode}")
    fun passwordRecovery(
        @PathVariable("activationCode")
        activationCode: String,
        model: Model
    ): String {
        model.addAttribute("code", activationCode)
        return "recovery"
    }
}
