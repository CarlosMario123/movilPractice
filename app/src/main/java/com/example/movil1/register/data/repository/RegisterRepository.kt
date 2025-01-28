package com.example.movil1.register.data.repository

import com.example.movil1.core.network.RetrofitHelper
import com.example.movil1.register.data.model.CreateUserRequest
import com.example.movil1.register.data.model.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Repositorio que maneja las operaciones de registro de usuarios.
 */
class RegisterRepository {
    private val registerApi = RetrofitHelper.getRetrofit()

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    suspend fun validateEmail(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = registerApi.validateUsername(email)
                handleApiResponse(response) { it.success }
            } catch (e: Exception) {
                Result.Error("Error al validar el email: ${e.message}")
            }
        }
    }

    suspend fun createUser(request: CreateUserRequest): Result<UserDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val response = registerApi.createUser(request)
                handleApiResponse(response) { it }
            } catch (e: Exception) {
                Result.Error("Error al crear el usuario: ${e.message}")
            }
        }
    }

    private fun <T, R> handleApiResponse(
        response: Response<T>,
        transform: (T) -> R
    ): Result<R> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(transform(it))
            } ?: Result.Error("Respuesta vacía del servidor")
        } else {
            Result.Error("Error: ${response.code()} - ${response.message()}")
        }
    }
}