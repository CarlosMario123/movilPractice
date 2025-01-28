package com.example.movil1.register.data.model

data class CreateUserRequest(
    val nombre: String,
    val email: String,
    val password: String
)