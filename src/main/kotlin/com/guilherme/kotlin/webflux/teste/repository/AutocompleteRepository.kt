package com.guilherme.kotlin.webflux.teste.repository

import com.guilherme.kotlin.webflux.teste.model.PartnerAutocomplete
import org.springframework.data.jpa.repository.JpaRepository

interface AutocompleteRepository: JpaRepository<PartnerAutocomplete, String>