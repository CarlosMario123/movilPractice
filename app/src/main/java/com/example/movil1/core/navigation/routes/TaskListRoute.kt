package com.example.movil1.core.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.core.navigation.destinations.Destinations
import com.example.movil1.taskList.presentation.TaskListScreen
import com.example.movil1.taskList.presentation.TaskListViewModelFactory

fun NavGraphBuilder.taskListRoute(
    navController: NavHostController
) {
    composable(route = Destinations.TaskList.route) {
        TaskListScreen(
            viewModel = viewModel(
                factory = TaskListViewModelFactory(LocalContext.current)
            )
        )
    }
}