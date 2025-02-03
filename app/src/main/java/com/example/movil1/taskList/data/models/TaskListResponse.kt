package com.example.movil1.taskList.data.models

data class TaskListResponse(
    val id: Int,
    val title: String,
    val description: String,
    val creator_id: Int
)
