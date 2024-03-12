package ru.kaplaan.notes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotesServerApplication

fun main(args: Array<String>) {
	runApplication<NotesServerApplication>(*args)
}
