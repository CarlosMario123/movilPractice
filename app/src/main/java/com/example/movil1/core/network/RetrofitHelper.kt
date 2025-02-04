package com.example.movil1.core.network

import com.example.movil1.core.storage.TokenManager
import com.example.movil1.login.data.datasource.LoginApi
import com.example.movil1.register.data.datasource.RegisterApi
import com.example.movil1.taskCreate.data.datasource.TaskApiCreate
import com.example.movil1.taskList.data.datasource.TaskDeleteApi
import com.example.movil1.taskList.data.datasource.TaskListApi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
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

    // Sin autenticación
    fun getRetrofit(): RegisterApi = retrofit.create(RegisterApi::class.java)
    fun getLoginApi(): LoginApi = retrofit.create(LoginApi::class.java)

    // Con autenticación
    fun getTaskApi(tokenManager: TokenManager): TaskApiCreate =
        createAuthenticatedRetrofit(tokenManager).create(TaskApiCreate::class.java)

    fun getTaskListApi(tokenManager: TokenManager): TaskListApi =
        createAuthenticatedRetrofit(tokenManager).create(TaskListApi::class.java)

    fun getTaskDeleteApi(tokenManager: TokenManager): TaskDeleteApi {
        return createAuthenticatedRetrofit(tokenManager).create(TaskDeleteApi::class.java)
    }
}