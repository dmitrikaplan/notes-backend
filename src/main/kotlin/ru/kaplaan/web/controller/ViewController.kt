package ru.kaplaan.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/auth")
class ViewController {
    @GetMapping("{activationCode}")
    fun passwordRecovery(@PathVariable("activationCode") code: String, model: Model): String {
        model.addAttribute("code", code)
        return "recovery"
    }
}
