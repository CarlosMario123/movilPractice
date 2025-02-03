// TaskListMapper.kt
package com.example.movil1.taskList.data.mapper

import com.example.movil1.taskList.data.models.TaskListDto
import com.example.movil1.taskList.data.models.TaskListResponse

object TaskListMapper {
    fun mapToDto(response: TaskListResponse): TaskListDto {
        return TaskListDto(
            id = response.id,
            title = response.title,
            description = response.description,
            creatorId = response.creator_id
        )
    }
}