package com.ets.preduzmi.util

import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.responses.GenericResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun saveBusiness(id: Int, success: () -> Unit, failure: (String?) -> Unit) {
    val token = "Bearer ${GlobalData.getToken()}"
    Api.service.saveBusiness(token, id).enqueue(object : Callback<GenericResponse<String>> {
        override fun onResponse(
            call: Call<GenericResponse<String>>,
            response: Response<GenericResponse<String>>
        ) {
            success()
        }

        override fun onFailure(call: Call<GenericResponse<String>>, t: Throwable) {
            failure(t.message)
        }
    })
}