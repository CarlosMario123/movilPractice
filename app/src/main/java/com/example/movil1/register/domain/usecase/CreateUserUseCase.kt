package com.example.movil1.register.domain.usecase


import UserMapper.toCreateUserRequest
import UserMapper.toDomain
import com.example.movil1.register.data.repository.RegisterRepository
import com.example.movil1.register.domain.model.User

class CreateUserUseCase(private val repository: RegisterRepository) {
    suspend operator fun invoke(user: User, password: String): Result<User> {
        return try {
            when (val result = repository.createUser(user.toCreateUserRequest(password))) {
                is RegisterRepository.Result.Success -> {
                    Result.success(result.data.toDomain())
                }
                is RegisterRepository.Result.Error -> {
                    Result.failure(Exception(result.message))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}