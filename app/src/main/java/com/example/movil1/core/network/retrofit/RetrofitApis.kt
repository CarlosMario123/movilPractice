package com.example.movil1.core.network.retrofit

import com.example.movil1.core.storage.TokenManager
import com.example.movil1.login.data.datasource.LoginApi
import com.example.movil1.register.data.datasource.RegisterApi
import com.example.movil1.taskCreate.data.datasource.TaskApiCreate
import com.example.movil1.taskList.data.datasource.TaskDeleteApi
import com.example.movil1.taskList.data.datasource.TaskListApi

object RetrofitApis {
    val retrofit = RetrofitBase.provideRetrofit()

    fun getRetrofit(): RegisterApi = retrofit.create(RegisterApi::class.java)
    fun getLoginApi(): LoginApi = retrofit.create(LoginApi::class.java)

    // Con autenticación
    fun getTaskApi(tokenManager: TokenManager): TaskApiCreate =
        RetrofitAuth.createAuthenticatedRetrofit(tokenManager).create(TaskApiCreate::class.java)

    fun getTaskListApi(tokenManager: TokenManager): TaskListApi =
        RetrofitAuth.createAuthenticatedRetrofit(tokenManager).create(TaskListApi::class.java)

    fun getTaskDeleteApi(tokenManager: TokenManager): TaskDeleteApi {
        return RetrofitAuth.createAuthenticatedRetrofit(tokenManager).create(TaskDeleteApi::class.java)
    }
}