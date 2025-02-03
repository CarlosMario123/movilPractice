package com.example.movil1.login.data.datasource

import com.example.movil1.login.data.model.LoginRequest
import com.example.movil1.login.data.model.LoginResponse
import retrofit2.Response

class LoginDataSource(private val api: LoginApi) {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }
}