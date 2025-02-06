package com.example.movil1.taskList.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.core.utils.VibrationService
import com.example.movil1.taskDelete.data.repository.TaskDeleteRepository
import com.example.movil1.taskList.data.repository.TaskListRepository

class TaskListViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            val taskListRepository = TaskListRepository(tokenManager)
            val taskDeleteRepository = TaskDeleteRepository(tokenManager)
            val vibrationService = VibrationService(context)
            @Suppress("UNCHECKED_CAST")
            return TaskListViewModel(
                taskListRepository,
                taskDeleteRepository,
                vibrationService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}