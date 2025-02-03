package com.example.movil1.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.movil1.login.presentation.LoginScreen
import com.example.movil1.login.presentation.LoginViewModelFactory
import com.example.movil1.register.presentation.RegisterScreen
import com.example.movil1.taskCreate.presentation.TaskCreateScreen
import com.example.movil1.taskCreate.presentation.TaskCreateViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Destinations.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Destinations.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Destinations.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.CreateTask.route) {
                        // Limpia el stack de navegación para que el usuario no pueda
                        // volver al login usando el botón back
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    factory = LoginViewModelFactory(LocalContext.current)
                )
            )
        }

        composable(route = Destinations.Register.route) {
            RegisterScreen(
                viewModel = viewModel(),
                onNavigateToLogin = {
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Destinations.CreateTask.route) {
            TaskCreateScreen(
                viewModel = viewModel(
                    factory = TaskCreateViewModelFactory(LocalContext.current)
                ),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}