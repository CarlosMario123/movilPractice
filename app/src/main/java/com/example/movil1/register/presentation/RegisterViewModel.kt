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
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    private val _nombre = MutableLiveData("")
    val nombre: LiveData<String> = _nombre

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _emailError = MutableLiveData<String?>(null)
    val emailError: LiveData<String?> = _emailError

    fun onNombreChanged(newNombre: String) {
        _nombre.value = newNombre
    }

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
                }
            }
            is RegisterRepository.Result.Error -> {
                _emailError.value = "Error al validar el email"
            }
        }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val nombre = _nombre.value ?: ""
            val email = _email.value ?: ""
            val password = _password.value ?: ""

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                _uiState.value = UiState.Error("Todos los campos son requeridos")
                return@launch
            }

            val request = CreateUserRequest(
                nombre = nombre,
                email = email,
                password = password
            )

            when (val result = registerRepository.createUser(request)) {
                is RegisterRepository.Result.Success -> {
                    _uiState.value = UiState.Success("Usuario creado exitosamente")
                }
                is RegisterRepository.Result.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
            }
        }
    }
}