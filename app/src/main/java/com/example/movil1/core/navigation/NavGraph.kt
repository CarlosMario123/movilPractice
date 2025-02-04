package com.example.movil1.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movil1.core.navigation.components.TaskNavigation
import com.example.movil1.core.navigation.routes.*
import com.example.movil1.core.navigation.destinations.Destinations

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
            loginRoute(navController)
            registerRoute(navController)
            createTaskRoute(navController)
            taskListRoute(navController)
        }
    }
}