package com.example.movil1.taskCreate.data.datasource

import com.example.movil1.taskCreate.data.models.CreateTaskRequest
import com.example.movil1.taskCreate.data.models.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskApiCreate {
    @POST("tasks/")
    suspend fun createTask(@Body request: CreateTaskRequest): Response<TaskResponse>
}