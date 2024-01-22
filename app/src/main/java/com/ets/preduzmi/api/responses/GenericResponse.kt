package com.ets.preduzmi.api.responses

import com.google.gson.annotations.SerializedName

data class GenericResponse<T> (
    val status: String,
    val code: Int,

    val data: T?,

    @SerializedName("message")
    val error: String?
)
