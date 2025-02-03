package com.example.movil1.taskCreate.data.models

data class TaskResponse(
    val id: Int,
    val title: String,
    val description: String,
    val creator_id: Int
)