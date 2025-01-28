package com.example.movil1.register.data.model

data class UserDTO(
    val nombre: String,
    val email: String
    // No incluimos password en la respuesta por seguridad
)