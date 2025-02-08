package com.example.movil1.register.data.repository

import com.example.movil1.core.network.retrofit.RetrofitApis
import com.example.movil1.register.data.model.CreateUserRequest
import com.example.movil1.register.data.model.UserDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Repositorio que maneja las operaciones de registro de usuarios.
 */
class RegisterRepository {
    private val registerApi = RetrofitApis.getRetrofit()
    private val gson = Gson()

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        sealed class Error : Result<Nothing>() {
            data class BadRequest(val message: String, val errors: List<String>? = null) : Error()
            data class NetworkError(val message: String) : Error()
            data class ServerError(val code: Int, val message: String) : Error()
        }
    }

    data class ErrorResponse(
        val message: String,
        val errors: List<String>? = null
    )

    suspend fun validateEmail(email: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = registerApi.validateUsername(email)
                handleApiResponse(response) { it.success }
            } catch (e: Exception) {
                Result.Error.NetworkError("Error de red: ${e.message}")
            }
        }
    }

    suspend fun createUser(request: CreateUserRequest): Result<UserDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val response = registerApi.createUser(request)
                handleApiResponse(response) { it }
            } catch (e: Exception) {
                Result.Error.NetworkError("Error al crear el usuario: ${e.message}")
            }
        }
    }

    private fun <T, R> handleApiResponse(
        response: Response<T>,
        transform: (T) -> R
    ): Result<R> {
        return when (response.code()) {
            in 200..299 -> {
                response.body()?.let {
                    Result.Success(transform(it))
                } ?: Result.Error.ServerError(500, "Respuesta vacía del servidor")
            }
            400 -> {
                try {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    Result.Error.BadRequest(
                        message = errorResponse.message,
                        errors = errorResponse.errors
                    )
                } catch (e: Exception) {
                    Result.Error.BadRequest(
                        message = "Error en la solicitud",
                        errors = null
                    )
                }
            }
            in 401..499 -> {
                Result.Error.ServerError(
                    code = response.code(),
                    message = "Error de cliente: ${response.message()}"
                )
            }
            else -> {
                Result.Error.ServerError(
                    code = response.code(),
                    message = "Error de servidor: ${response.message()}"
                )
            }
        }
    }
}