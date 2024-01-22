package com.ets.preduzmi.api

import com.ets.preduzmi.api.requests.BusinessesRequest
import com.ets.preduzmi.api.requests.CreateBusinessRequest
import com.ets.preduzmi.api.requests.EditBusinessRequest
import com.ets.preduzmi.api.requests.EditUserRequest
import com.ets.preduzmi.api.requests.RegisterRequest
import com.ets.preduzmi.api.requests.LoginRequest
import com.ets.preduzmi.api.responses.GenericResponse
import com.ets.preduzmi.api.responses.SearchResponse
import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.models.UserModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory
import com.ets.preduzmi.util.GlobalData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object Api {
    private val retrofit: Retrofit

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val headersInterceptor = Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.header("Accept", "application/json")
            chain.proceed(requestBuilder.build())
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(GlobalData.API_PREFIX)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ApiInterface = retrofit.create()
}

interface ApiInterface {
    @POST("auth/login")
    fun login(@Body body: LoginRequest): Call<GenericResponse<String>>

    @POST("auth/register")
    fun register(@Body body: RegisterRequest): Call<GenericResponse<String>>

    @GET("user")
    fun user(@Header("Authorization") token: String): Call<GenericResponse<UserModel>>

    @PUT("user/edit")
    fun editUser(@Header("Authorization") token: String, @Body body: EditUserRequest): Call<GenericResponse<UserModel>>

    @POST("business/all")
    fun businesses(@Header("Authorization") token: String, @Body body: BusinessesRequest): Call<GenericResponse<ArrayList<BusinessModel>>>

    @POST("business/create")
    fun createBusiness(@Header("Authorization") token: String, @Body body: CreateBusinessRequest): Call<GenericResponse<BusinessModel>>

    @PUT("business/edit/{id}")
    fun editBusiness(@Header("Authorization") token: String, @Path("id") businessId: Int, @Body body: EditBusinessRequest): Call<GenericResponse<BusinessModel>>

    @PUT("business/like/{id}")
    fun likeBusiness(@Header("Authorization") token: String, @Path("id") businessId: Int): Call<GenericResponse<String>>

    @PUT("business/save/{id}")
    fun saveBusiness(@Header("Authorization") token: String, @Path("id") businessId: Int): Call<GenericResponse<String>>

    @GET("user/posted")
    fun userPostedBusinesses(@Header("Authorization") token: String): Call<GenericResponse<ArrayList<BusinessModel>>>

    @GET("user/liked")
    fun userLikedBusinesses(@Header("Authorization") token: String): Call<GenericResponse<ArrayList<BusinessModel>>>

    @GET("user/saved")
    fun userSavedBusinesses(@Header("Authorization") token: String): Call<GenericResponse<ArrayList<BusinessModel>>>

    @GET("search")
    fun search(@Header("Authorization") token: String, @Query("query") query: String): Call<GenericResponse<SearchResponse>>
}