package com.example.movil1.taskCreate.data.models

data class TaskCreateDto(
    val id: Int,
    val title: String,
    val description: String,
    val creatorId: Int
)