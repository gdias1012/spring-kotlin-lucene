package com.guilherme.kotlin.webflux.teste.config

import com.guilherme.kotlin.webflux.teste.handler.UserHandler
import com.guilherme.kotlin.webflux.teste.service.AutocompleteService
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.RouterFunctions

fun beans() = beans {
    bean("webHandler") {
        RouterFunctions.toWebHandler(ref<Router>().router())
    }
    bean<Router>()
    bean<UserHandler>()
    bean<AutocompleteService>()
//    profile("cors") {
//        bean("corsFilter") {
//            CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() }
//        }
//    }
}