package com.ets.preduzmi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ets.preduzmi.api.Api
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.api.responses.SearchResponse
import com.ets.preduzmi.util.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    var query: String = ""

    private val _results = MutableLiveData<SearchResponse>()
    val results: LiveData<SearchResponse> = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun search(query: String) {
        this.query = query

        val token = "Bearer ${GlobalData.getToken()}"
        Api.service.search(token, query).enqueue(object :
            Callback<GenericResponse<SearchResponse>> {
            override fun onResponse(call: Call<GenericResponse<SearchResponse>>, response: Response<GenericResponse<SearchResponse>>) {
                if (response.code() == 200) {
                    _results.postValue(response.body()!!.data!!)
                }
            }

            override fun onFailure(call: Call<GenericResponse<SearchResponse>>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}