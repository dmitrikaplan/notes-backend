package ru.kaplaan.domain.domain.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    servers = [
        Server(
            url = "https://kaplaan.ru/backend/app",
            description = "Default server url"
        )
    ],
    info = Info(
        title = "Notes Api",
        description = "Api для приложения заметок",
        version = "1.0.0",
        contact = Contact(
            name = "Dmitry Kaplan",
            email = "dmitry@kaplaan.ru",
            url = "https://kaplaan.ru/backend/app"
        )
    )
)
@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenApiConfig