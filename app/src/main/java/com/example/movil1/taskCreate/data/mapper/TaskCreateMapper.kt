package com.example.movil1.taskCreate.data.mapper

import com.example.movil1.taskCreate.data.models.TaskCreateDto
import com.example.movil1.taskCreate.data.models.TaskResponse

object TaskCreateMapper {

    fun mapToDto(response: TaskResponse): TaskCreateDto {
        return TaskCreateDto(
            id = response.id,
            title = response.title,
            description = response.description,
            creatorId = response.creator_id
        )
    }
}