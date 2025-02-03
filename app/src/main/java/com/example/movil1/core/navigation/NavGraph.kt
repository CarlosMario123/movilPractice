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
    }
}