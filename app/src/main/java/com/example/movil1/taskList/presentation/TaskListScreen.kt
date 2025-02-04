package com.example.movil1.taskList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.shared.components.LoadingOverlay
import com.example.movil1.shared.components.TextToken
import com.example.movil1.taskList.presentation.components.TaskItem
import com.example.movil1.ui.theme.AppTheme

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.observeAsState(TaskListViewModel.UiState.Initial)
    val deleteTaskState by viewModel.deleteTaskState.observeAsState(TaskListViewModel.DeleteTaskState.Initial)
    val tokenManager = TokenManager(LocalContext.current)
    val currentToken = remember { tokenManager.getToken() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (currentToken.isNullOrEmpty()) {
            snackbarHostState.showSnackbar("No hay token de autenticación")
            onNavigateBack()
        }
    }

    LaunchedEffect(deleteTaskState) {
        when (deleteTaskState) {
            is TaskListViewModel.DeleteTaskState.Success -> {
                snackbarHostState.showSnackbar("Tarea eliminada exitosamente")
            }
            is TaskListViewModel.DeleteTaskState.Error -> {
                snackbarHostState.showSnackbar("Error no se elimino la tarea")
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Mis Tareas",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        currentToken?.take(20)?.let { TextToken(it) }

                        when (val state = uiState) {
                            is TaskListViewModel.UiState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            is TaskListViewModel.UiState.Success -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.tasks) { task ->
                                        TaskItem(
                                            task = task,
                                            onDeleteClick = { taskId ->
                                                viewModel.deleteTask(taskId)
                                            }
                                        )
                                    }
                                }
                            }
                            is TaskListViewModel.UiState.Error -> {
                                Text(
                                    text = state.message,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }

            if (deleteTaskState is TaskListViewModel.DeleteTaskState.Loading) {
                LoadingOverlay(message = "Eliminando tarea...")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    AppTheme {
        TaskListScreen(
            viewModel = viewModel(
                factory = TaskListViewModelFactory(LocalContext.current)
            )
        )
    }
}