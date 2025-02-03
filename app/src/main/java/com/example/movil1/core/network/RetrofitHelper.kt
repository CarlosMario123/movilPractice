package com.example.movil1.core.network

import com.example.movil1.login.data.datasource.LoginApi
import com.example.movil1.register.data.datasource.RegisterApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://3.220.33.37:8000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit(): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    fun getLoginApi(): LoginApi {
        return retrofit.create(LoginApi::class.java)

    }
}