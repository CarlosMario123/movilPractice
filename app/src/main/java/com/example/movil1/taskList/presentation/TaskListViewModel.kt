package com.example.movil1.taskList.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil1.taskList.data.repository.TaskListRepository
import com.example.movil1.taskList.data.models.TaskListDto
import com.example.movil1.taskDelete.data.repository.TaskDeleteRepository
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskListRepository: TaskListRepository,
    private val taskDeleteRepository: TaskDeleteRepository
) : ViewModel() {

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val tasks: List<TaskListDto>) : UiState()
        data class Error(val message: String, val errors: List<String>? = null) : UiState()
    }

    sealed class DeleteTaskState {
        object Initial : DeleteTaskState()
        object Loading : DeleteTaskState()
        object Success : DeleteTaskState()
        data class Error(val message: String) : DeleteTaskState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    private val _deleteTaskState = MutableLiveData<DeleteTaskState>(DeleteTaskState.Initial)
    val deleteTaskState: LiveData<DeleteTaskState> = _deleteTaskState

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                when (val result = taskListRepository.getTasks()) {
                    is TaskListRepository.Result.Success -> {
                        _uiState.value = UiState.Success(result.data)
                    }
                    is TaskListRepository.Result.Error.BadRequest -> {
                        _uiState.value = UiState.Error(
                            message = result.message,
                            errors = result.errors
                        )
                    }
                    is TaskListRepository.Result.Error.NetworkError -> {
                        _uiState.value = UiState.Error(
                            message = "Error de conexión: ${result.message}"
                        )
                    }
                    is TaskListRepository.Result.Error.ServerError -> {
                        _uiState.value = UiState.Error(
                            message = "Error del servidor: ${result.code} - ${result.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = "Error inesperado",
                    errors = listOf(e.message ?: "Error desconocido")
                )
            }
        }
    }

    // En TaskListViewModel, modifica el método deleteTask:
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                _deleteTaskState.value = DeleteTaskState.Loading

                when (val result = taskDeleteRepository.deleteTask(taskId)) {
                    is TaskDeleteRepository.Result.Success -> {
                        _deleteTaskState.value = DeleteTaskState.Success
                        Log.d("task","hizo el load")
                        loadTasks()
                    }
                    is TaskDeleteRepository.Result.Error.BadRequest -> {
                        _deleteTaskState.value = DeleteTaskState.Error(result.message)
                    }
                    is TaskDeleteRepository.Result.Error.NetworkError -> {
                        _deleteTaskState.value = DeleteTaskState.Error("Error de conexión: ${result.message}")
                    }
                    is TaskDeleteRepository.Result.Error.ServerError -> {
                        _deleteTaskState.value = DeleteTaskState.Error("Error del servidor: ${result.code} - ${result.message}")
                    }
                }
            } catch (e: Exception) {
                _deleteTaskState.value = DeleteTaskState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Initial
        _deleteTaskState.value = DeleteTaskState.Initial
    }
}