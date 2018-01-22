package com.guilherme.kotlin.webflux.teste.config

import com.guilherme.kotlin.webflux.teste.handler.UserHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

class Router(private val userHandler: UserHandler) {

    fun router() = router {
        (accept(MediaType.APPLICATION_JSON) and "/user").nest {
            GET("/", userHandler::vai)
            GET("/search", userHandler::search)
        }
    }

}