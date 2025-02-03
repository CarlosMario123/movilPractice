package com.example.movil1.core.navigation


sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object Register : Destinations("register")

}