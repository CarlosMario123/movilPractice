package com.example.movil1.taskList.data.repository

import com.example.movil1.core.network.retrofit.RetrofitApis
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.taskList.data.mapper.TaskListMapper
import com.example.movil1.taskList.data.models.TaskListDto
import com.example.movil1.taskList.data.models.TaskListResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TaskListRepository(private val tokenManager: TokenManager) {
    private val taskListApi = RetrofitApis.getTaskListApi(tokenManager)
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

    suspend fun getTasks(): Result<List<TaskListDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskListApi.getTasks()
                handleApiResponse<List<TaskListResponse>, List<TaskListDto>>(response) { tasks ->
                    tasks.map { TaskListMapper.mapToDto(it) }
                }
            } catch (e: Exception) {
                Result.Error.NetworkError("Error de red: ${e.message}")
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
            307 -> {
                val newUrl = response.headers()["Location"]
                if (newUrl != null) {
                    Result.Error.NetworkError("Recurso movido temporalmente a: $newUrl")

                } else {
                    Result.Error.ServerError(307, "Redirección sin ubicación proporcionada")
                }
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
                    Result.Error.BadRequest("Error en la solicitud")
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