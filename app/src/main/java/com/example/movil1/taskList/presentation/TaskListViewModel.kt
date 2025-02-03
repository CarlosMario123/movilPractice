package com.example.movil1.taskList.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil1.taskList.data.repository.TaskListRepository
import com.example.movil1.taskList.data.models.TaskListDto
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskListRepository: TaskListRepository
) : ViewModel() {

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val tasks: List<TaskListDto>) : UiState()
        data class Error(val message: String, val errors: List<String>? = null) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

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

    fun resetState() {
        _uiState.value = UiState.Initial
    }
}