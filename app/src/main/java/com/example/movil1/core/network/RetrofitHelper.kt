package com.example.movil1.core.network

import com.example.movil1.core.storage.TokenManager
import com.example.movil1.login.data.datasource.LoginApi
import com.example.movil1.register.data.datasource.RegisterApi
import com.example.movil1.taskCreate.data.datasource.TaskApiCreate

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://3.220.33.37:8000/"

    // Para endpoints sin autenticación
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Para endpoints con autenticación
    private fun createAuthenticatedRetrofit(tokenManager: TokenManager): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = tokenManager.getToken()

                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest
                }

                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Endpoints sin autenticación
    fun getRetrofit(): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    fun getLoginApi(): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    // Endpoints con autenticación
    fun getTaskApi(tokenManager: TokenManager): TaskApiCreate {
        return createAuthenticatedRetrofit(tokenManager).create(TaskApiCreate::class.java)
    }
}