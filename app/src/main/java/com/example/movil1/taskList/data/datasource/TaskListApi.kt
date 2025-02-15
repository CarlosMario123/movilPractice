package com.example.movil1.taskList.data.datasource

import com.example.movil1.taskList.data.models.TaskListResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskListApi {
    @GET("tasks")
    suspend fun getTasks(): Response<List<TaskListResponse>>

    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}