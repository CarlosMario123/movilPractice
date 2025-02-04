package com.example.movil1.core.navigation.routes
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.core.navigation.destinations.Destinations
import com.example.movil1.taskCreate.presentation.TaskCreateScreen
import com.example.movil1.taskCreate.presentation.TaskCreateViewModelFactory

fun NavGraphBuilder.createTaskRoute(
    navController: NavHostController
) {
    composable(route = Destinations.CreateTask.route) {
        TaskCreateScreen(
            viewModel = viewModel(
                factory = TaskCreateViewModelFactory(LocalContext.current)
            ),
            onNavigateBack = { navController.popBackStack() }
        )
    }
}