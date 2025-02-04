package com.example.movil1.core.navigation.routes


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.core.navigation.destinations.Destinations
import com.example.movil1.register.presentation.RegisterScreen

fun NavGraphBuilder.registerRoute(
    navController: NavHostController
) {
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