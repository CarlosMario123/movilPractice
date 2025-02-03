package com.example.movil1.taskList.data.datasource

import com.example.movil1.taskList.data.models.TaskListResponse
import retrofit2.Response
import retrofit2.http.GET

interface TaskListApi {
    @GET("tasks")
    suspend fun getTasks(): Response<List<TaskListResponse>>
}