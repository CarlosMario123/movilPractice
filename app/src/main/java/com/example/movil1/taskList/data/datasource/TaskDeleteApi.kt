package com.example.movil1.taskList.data.datasource

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface TaskDeleteApi {
    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}