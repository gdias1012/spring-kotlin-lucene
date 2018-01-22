package com.guilherme.kotlin.webflux.teste.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PARTNER_AUTOCOMPLETE")
class PartnerAutocomplete(@Id val id: String, val name: String, val type: String)