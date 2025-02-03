package com.example.movil1.login.data.datasource

import com.example.movil1.login.data.model.LoginRequest
import com.example.movil1.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}