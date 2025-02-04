package com.example.movil1.core.navigation.routes
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.core.navigation.destinations.Destinations
import com.example.movil1.login.presentation.LoginScreen
import com.example.movil1.login.presentation.LoginViewModelFactory

fun NavGraphBuilder.loginRoute(
    navController: NavHostController
) {
    composable(route = Destinations.Login.route) {
        LoginScreen(
            onNavigateToRegister = {
                navController.navigate(Destinations.Register.route)
            },
            onLoginSuccess = {
                navController.navigate(Destinations.TaskList.route) {
                    popUpTo(Destinations.Login.route) { inclusive = true }
                }
            },
            viewModel = viewModel(
                factory = LoginViewModelFactory(LocalContext.current)
            )
        )
    }
}