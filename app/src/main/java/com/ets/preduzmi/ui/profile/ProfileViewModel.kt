package com.ets.preduzmi.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.models.UserModel
import com.ets.preduzmi.util.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel> = _userData

    private val _likedBusinesses = MutableLiveData<ArrayList<BusinessModel>>()
    val likedBusinesses: LiveData<ArrayList<BusinessModel>> = _likedBusinesses

    private val _savedBusinesses = MutableLiveData<ArrayList<BusinessModel>>()
    val savedBusinesses: LiveData<ArrayList<BusinessModel>> = _savedBusinesses

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUserData() {
        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.user(token).enqueue(object :
            Callback<GenericResponse<UserModel>> {
            override fun onResponse(call: Call<GenericResponse<UserModel>>, response: Response<GenericResponse<UserModel>>) {
                if (response.code() == 200) {
                    _userData.postValue(response.body()!!.data!!)
                }
            }

            override fun onFailure(call: Call<GenericResponse<UserModel>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }

    fun loadLikedBusinesses() {
        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.userLikedBusinesses(token).enqueue(object :
            Callback<GenericResponse<ArrayList<BusinessModel>>> {
            override fun onResponse(call: Call<GenericResponse<ArrayList<BusinessModel>>>, response: Response<GenericResponse<ArrayList<BusinessModel>>>) {
                if (response.code() == 200) {
                    _likedBusinesses.postValue(response.body()!!.data!!)
                }
            }

            override fun onFailure(call: Call<GenericResponse<ArrayList<BusinessModel>>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }

    fun loadSavedBusinesses() {
        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.userSavedBusinesses(token).enqueue(object :
            Callback<GenericResponse<ArrayList<BusinessModel>>> {
            override fun onResponse(call: Call<GenericResponse<ArrayList<BusinessModel>>>, response: Response<GenericResponse<ArrayList<BusinessModel>>>) {
                if (response.code() == 200) {
                    _savedBusinesses.postValue(response.body()!!.data!!)
                }
            }

            override fun onFailure(call: Call<GenericResponse<ArrayList<BusinessModel>>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}