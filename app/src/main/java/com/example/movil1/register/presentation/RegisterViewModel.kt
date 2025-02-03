package com.example.movil1.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil1.register.data.model.CreateUserRequest
import com.example.movil1.register.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerRepository = RegisterRepository()

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val message: String, val errors: List<String>? = null) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState



    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _emailError = MutableLiveData<String?>(null)
    val emailError: LiveData<String?> = _emailError



    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onEmailFocusLost() {
        viewModelScope.launch {
            val email = _email.value ?: return@launch
            if (email.isNotEmpty()) {
                validateEmail(email)
            }
        }
    }

    private suspend fun validateEmail(email: String) {
        when (val result = registerRepository.validateEmail(email)) {
            is RegisterRepository.Result.Success -> {
                if (!result.data) {
                    _emailError.value = "Este email ya está en uso"
                } else {
                    _emailError.value = null
                }
            }
            is RegisterRepository.Result.Error.BadRequest -> {
                _emailError.value = result.message
            }
            is RegisterRepository.Result.Error.NetworkError -> {
                _emailError.value = "Error de conexión: ${result.message}"
            }
            is RegisterRepository.Result.Error.ServerError -> {
                _emailError.value = "Error del servidor: ${result.message}"
            }
        }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                val email = _email.value ?: ""
                val password = _password.value ?: ""

                // Validaciones
                val validationErrors = mutableListOf<String>()

                if (email.isEmpty()) validationErrors.add("El email es requerido")
                if (password.isEmpty()) validationErrors.add("La contraseña es requerida")

                if (validationErrors.isNotEmpty()) {
                    _uiState.value = UiState.Error(
                        message = "Por favor, complete todos los campos",
                        errors = validationErrors
                    )
                    return@launch
                }

                val request = CreateUserRequest(
                    email = email,
                    password = password
                )

                when (val result = registerRepository.createUser(request)) {
                    is RegisterRepository.Result.Success -> {
                        _uiState.value = UiState.Success("Usuario creado exitosamente")
                    }
                    is RegisterRepository.Result.Error.BadRequest -> {
                        _uiState.value = UiState.Error(
                            message = result.message,
                            errors = result.errors
                        )
                    }
                    is RegisterRepository.Result.Error.NetworkError -> {
                        _uiState.value = UiState.Error(
                            message = "Error de conexión: ${result.message}"
                        )
                    }
                    is RegisterRepository.Result.Error.ServerError -> {
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
}