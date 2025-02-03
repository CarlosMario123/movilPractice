package com.example.movil1.login.presentation

import LoginRepository
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movil1.core.storage.TokenManager
import android.util.Log

import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val token: String) : UiState()
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

    private val _passwordError = MutableLiveData<String?>(null)
    val passwordError: LiveData<String?> = _passwordError

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun onLoginClick() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                val email = _email.value ?: ""
                val password = _password.value ?: ""

                // Validaciones
                val validationErrors = mutableListOf<String>()

                if (email.isEmpty()) {
                    validationErrors.add("El email es requerido")
                    _emailError.value = "El email es requerido"
                }
                if (password.isEmpty()) {
                    validationErrors.add("La contraseña es requerida")
                    _passwordError.value = "La contraseña es requerida"
                }

                if (validationErrors.isNotEmpty()) {
                    _uiState.value = UiState.Error(
                        message = "Por favor, complete todos los campos",
                        errors = validationErrors
                    )
                    return@launch
                }

                when (val result = loginRepository.login(email, password)) {
                    is LoginRepository.Result.Success -> {
                        result.data.access_token.let { token ->
                            tokenManager.saveToken(token)
                            _uiState.value = UiState.Success(token)
                            clearFields()
                        }
                    }
                    is LoginRepository.Result.Error.BadRequest -> {
                        _uiState.value = UiState.Error(
                            message = result.message,
                            errors = result.errors
                        )
                    }

                    is LoginRepository.Result.Error.NetworkError -> {
                        _uiState.value = UiState.Error(
                            message = "Error de conexión: ${result.message}"
                        )
                        Log.e("LoginViewModel", "NetworkError: ${result.message}")
                        Log.e("LoginViewModel", "NetworkError: ${result.message}")


                    }
                    is LoginRepository.Result.Error.ServerError -> {
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
        _email.value = ""
        _password.value = ""
        _emailError.value = null
        _passwordError.value = null
    }

    fun resetState() {
        _uiState.value = UiState.Initial
        clearFields()
    }
}

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            val loginRepository = LoginRepository()
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginRepository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}