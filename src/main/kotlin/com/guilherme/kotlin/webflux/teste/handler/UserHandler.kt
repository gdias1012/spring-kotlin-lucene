package com.guilherme.kotlin.webflux.teste.handler

import com.guilherme.kotlin.webflux.teste.repository.UserRepository
import com.guilherme.kotlin.webflux.teste.service.AutocompleteService
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

class UserHandler(private val userRepository: UserRepository,
                  private val autocompleteService: AutocompleteService) {

    fun vai(req: ServerRequest) = ServerResponse.ok().body(fromObject(userRepository.findAll()[0]))

    fun search(req: ServerRequest) = ServerResponse.ok().body(fromObject(autocompleteService.search(req.queryParam("q").get(), req.queryParam( "limit").get().toInt())))

}