package com.ets.preduzmi.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.requests.EditUserRequest
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.models.UserModel
import com.ets.preduzmi.util.GlobalData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class EditProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun editProfile(
        data: EditUserRequest
    ) {
        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.editUser(token, data).enqueue(object :
            Callback<GenericResponse<UserModel>> {
            override fun onResponse(
                call: Call<GenericResponse<UserModel>>,
                response: Response<GenericResponse<UserModel>>
            ) {
                when (response.code()) {
                    200 -> {
                        _user.postValue(response.body()!!.data!!)
                    }

                    else -> {
                        val type: Type = object : TypeToken<GenericResponse<String>>() {}.type
                        val body: GenericResponse<String> = Gson().fromJson(response.errorBody()!!.string(), type)

                        _error.postValue(body.error!!)
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse<UserModel>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}