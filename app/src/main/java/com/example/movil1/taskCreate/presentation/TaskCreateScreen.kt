package com.example.movil1.taskCreate.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.R
import com.example.movil1.core.storage.TokenManager
import com.example.movil1.shared.components.TextToken
import com.example.movil1.ui.theme.AppTheme

@Composable
fun TaskCreateScreen(
    viewModel: TaskCreateViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val uiState by viewModel.uiState.observeAsState(TaskCreateViewModel.UiState.Initial)
    val titleError by viewModel.titleError.observeAsState(null)
    val descriptionError by viewModel.descriptionError.observeAsState(null)

    val tokenManager = TokenManager(LocalContext.current)
    val currentToken = remember { tokenManager.getToken() }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (currentToken.isNullOrEmpty()) {
            snackbarHostState.showSnackbar("No hay token de autenticación")
            onNavigateBack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Crear Tarea",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    currentToken?.take(20)?.let { TextToken(it) }//Es como un condicional si esta listo renderiza


                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { viewModel.onTitleChanged(it) },
                        isError = titleError != null,
                        label = { Text("Título") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.Create, contentDescription = "título")
                        },
                        supportingText = {
                            if (titleError != null) {
                                Text(
                                    text = titleError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.onDescriptionChanged(it) },
                        isError = descriptionError != null,
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(horizontal = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        ),
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = "descripción")
                        },
                        supportingText = {
                            if (descriptionError != null) {
                                Text(
                                    text = descriptionError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    Button(
                        onClick = { viewModel.onCreateTaskClick() },
                        enabled = uiState !is TaskCreateViewModel.UiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (uiState is TaskCreateViewModel.UiState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Crear Tarea",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            LaunchedEffect(uiState) {
                when (uiState) {
                    is TaskCreateViewModel.UiState.Success -> {
                        snackbarHostState.showSnackbar(
                            message = "Tarea creada exitosamente"
                        )
                        onNavigateBack()
                    }
                    is TaskCreateViewModel.UiState.Error -> {
                        snackbarHostState.showSnackbar(
                            message = (uiState as TaskCreateViewModel.UiState.Error).message
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCreateScreenPreview() {
    AppTheme {
        TaskCreateScreen(
            viewModel = viewModel(
                factory = TaskCreateViewModelFactory(LocalContext.current)
            ),
            onNavigateBack = {}
        )
    }
}