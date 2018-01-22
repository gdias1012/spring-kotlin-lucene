package com.guilherme.kotlin.webflux.teste.repository

import com.guilherme.kotlin.webflux.teste.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, UUID>