package com.ets.preduzmi.ui.edit_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.requests.EditBusinessRequest
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.util.GlobalData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class EditPostViewModel : ViewModel() {
    private val _business = MutableLiveData<BusinessModel>()
    val business: LiveData<BusinessModel> = _business

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun editBusiness(
        id: Int,
        data: EditBusinessRequest
    ) {
        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.editBusiness(token, id, data).enqueue(object : Callback<GenericResponse<BusinessModel>> {
            override fun onResponse(
                call: Call<GenericResponse<BusinessModel>>,
                response: Response<GenericResponse<BusinessModel>>
            ) {
                when (response.code()) {
                    200 -> {
                        _business.postValue(response.body()!!.data!!)
                    }

                    else -> {
                        val type: Type = object : TypeToken<GenericResponse<String>>() {}.type
                        val body: GenericResponse<String> = Gson().fromJson(response.errorBody()!!.string(), type)

                        _error.postValue(body.error!!)
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse<BusinessModel>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}