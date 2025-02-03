package com.example.movil1.core.navigation


sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object Register : Destinations("register")
    object CreateTask : Destinations("create_task")
    object TaskList : Destinations("task_list")

}