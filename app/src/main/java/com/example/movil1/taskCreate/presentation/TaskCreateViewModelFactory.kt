package com.example.movil1.taskCreate.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.taskCreate.data.repository.TaskCreateRepository

class TaskCreateViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskCreateViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            val taskCreateRepository = TaskCreateRepository(tokenManager)
            @Suppress("UNCHECKED_CAST")
            return TaskCreateViewModel(taskCreateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}