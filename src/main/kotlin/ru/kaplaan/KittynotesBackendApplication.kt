package ru.kaplaan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KittynotesBackendApplication


fun main(args: Array<String>){
    runApplication<ru.kaplaan.KittynotesBackendApplication>(*args)
}