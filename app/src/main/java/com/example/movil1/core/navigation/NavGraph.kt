package com.example.movil1.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import com.example.movil1.login.presentation.LoginScreen
import com.example.movil1.login.presentation.LoginViewModelFactory
import com.example.movil1.register.presentation.RegisterScreen
import com.example.movil1.taskCreate.presentation.TaskCreateScreen
import com.example.movil1.taskCreate.presentation.TaskCreateViewModelFactory
import com.example.movil1.taskList.presentation.TaskListScreen
import com.example.movil1.taskList.presentation.TaskListViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Destinations.Login.route
) {
    Scaffold(
        bottomBar = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
                    Destinations.CreateTask.route,
                    Destinations.TaskList.route
                )) {
                TaskNavigation(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
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
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(route = Destinations.TaskList.route) {
                TaskListScreen(
                    viewModel = viewModel(
                        factory = TaskListViewModelFactory(LocalContext.current)
                    )
                )
            }
        }
    }
}
