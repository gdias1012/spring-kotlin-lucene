package com.guilherme.kotlin.webflux.teste

import com.guilherme.kotlin.webflux.teste.config.beans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TesteApplication

fun main(args: Array<String>) {
    runApplication<TesteApplication>(*args) {
        addInitializers(beans())
    }
}
