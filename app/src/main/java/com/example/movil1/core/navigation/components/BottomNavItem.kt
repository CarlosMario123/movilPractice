package com.example.movil1.core.navigation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Create : BottomNavItem("create_task", Icons.Default.Add, "Crear")
    object List : BottomNavItem("task_list", Icons.Default.List, "Lista")
}