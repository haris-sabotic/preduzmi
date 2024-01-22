package com.ets.preduzmi.api.requests

data class RegisterRequest (
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
)
