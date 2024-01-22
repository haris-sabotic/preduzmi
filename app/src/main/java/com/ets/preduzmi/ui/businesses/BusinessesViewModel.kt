package com.ets.preduzmi.ui.businesses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.requests.BusinessesRequest
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.util.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessesViewModel : ViewModel() {
    private val _businesses = MutableLiveData<ArrayList<BusinessModel>>()
    val businesses: LiveData<ArrayList<BusinessModel>> = _businesses

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadBusinesses(types: ArrayList<String>?, legalTypes: ArrayList<String>?) {
        Log.d("LOADING BUSINESSES (types)", types.toString())
        Log.d("LOADING BUSINESSES (legalTypes)", legalTypes.toString())

        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.businesses(token, BusinessesRequest(types, legalTypes)).enqueue(object :
            Callback<GenericResponse<ArrayList<BusinessModel>>> {
            override fun onResponse(call: Call<GenericResponse<ArrayList<BusinessModel>>>, response: Response<GenericResponse<ArrayList<BusinessModel>>>) {
                if (response.code() == 200) {
                    _businesses.postValue(response.body()!!.data!!)
                }
            }

            override fun onFailure(call: Call<GenericResponse<ArrayList<BusinessModel>>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}