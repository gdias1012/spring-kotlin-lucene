package com.guilherme.kotlin.webflux.teste.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PORTAL_USER")
class User(@Id val id: UUID, val name: String)