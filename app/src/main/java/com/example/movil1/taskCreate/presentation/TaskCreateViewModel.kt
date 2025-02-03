package com.example.movil1.taskCreate.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil1.taskCreate.data.repository.TaskCreateRepository
import kotlinx.coroutines.launch

class TaskCreateViewModel(
    private val taskCreateRepository: TaskCreateRepository
) : ViewModel() {

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val message: String, val errors: List<String>? = null) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _titleError = MutableLiveData<String?>(null)
    val titleError: LiveData<String?> = _titleError

    private val _descriptionError = MutableLiveData<String?>(null)
    val descriptionError: LiveData<String?> = _descriptionError

    fun onTitleChanged(newTitle: String) {
        _title.value = newTitle
        _titleError.value = null
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
        _descriptionError.value = null
    }

    fun onCreateTaskClick() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                val title = _title.value ?: ""
                val description = _description.value ?: ""


                val validationErrors = mutableListOf<String>()

                if (title.isEmpty()) {
                    validationErrors.add("El título es requerido")
                    _titleError.value = "El título es requerido"
                }
                if (description.isEmpty()) {
                    validationErrors.add("La descripción es requerida")
                    _descriptionError.value = "La descripción es requerida"
                }

                if (validationErrors.isNotEmpty()) {
                    _uiState.value = UiState.Error(
                        message = "Por favor, complete todos los campos",
                        errors = validationErrors
                    )
                    return@launch
                }

                when (val result = taskCreateRepository.createTask(title, description)) {
                    is TaskCreateRepository.Result.Success -> {
                        _uiState.value = UiState.Success("Tarea creada exitosamente")
                        clearFields()
                    }
                    is TaskCreateRepository.Result.Error.BadRequest -> {
                        _uiState.value = UiState.Error(
                            message = result.message,
                            errors = result.errors
                        )
                    }
                    is TaskCreateRepository.Result.Error.NetworkError -> {
                        _uiState.value = UiState.Error(
                            message = "Error de conexión: ${result.message}"
                        )
                    }
                    is TaskCreateRepository.Result.Error.ServerError -> {
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

    private fun clearFields() {
        _title.value = ""
        _description.value = ""
        _titleError.value = null
        _descriptionError.value = null
    }

    fun resetState() {
        _uiState.value = UiState.Initial
        clearFields()
    }
}