package com.example.movil1.taskList.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.taskList.data.repository.TaskListRepository

class TaskListViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            val taskListRepository = TaskListRepository(tokenManager)
            @Suppress("UNCHECKED_CAST")
            return TaskListViewModel(taskListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}