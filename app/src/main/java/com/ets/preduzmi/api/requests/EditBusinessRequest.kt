package com.ets.preduzmi.api.requests

data class EditBusinessRequest (
    val name: String?,
    val type: String?,
    val legalType: String?,
    val description: String?,
    val photo: String?,
)
