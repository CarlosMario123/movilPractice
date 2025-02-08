package com.example.movil1.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movil1.R
import com.example.movil1.shared.components.LoadingOverlay
import com.example.movil1.ui.theme.AppTheme

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel
) {
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val uiState by viewModel.uiState.observeAsState(LoginViewModel.UiState.Initial)
    val emailError by viewModel.emailError.observeAsState(null)
    val passwordError by viewModel.passwordError.observeAsState(null)

    val snackbarHostState = remember { SnackbarHostState() }

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
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = "TaskApp",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Image(
                            painter = painterResource(id = R.drawable.task),
                            contentDescription = "task",
                            modifier = Modifier
                                .width(260.dp)
                                .height(260.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                .padding(20.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.onEmailChanged(it) },
                            isError = emailError != null,
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            ),
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = "email")
                            },
                            supportingText = {
                                if (emailError != null) {
                                    Text(
                                        text = emailError!!,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.onPasswordChanged(it) },
                            isError = passwordError != null,
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            ),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = "password")
                            },
                            supportingText = {
                                if (passwordError != null) {
                                    Text(
                                        text = passwordError!!,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )

                        Button(
                            onClick = { viewModel.onLoginClick() },
                            enabled = uiState !is LoginViewModel.UiState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (uiState is LoginViewModel.UiState.Loading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    "Login",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Text(
                            text = "Create new account",
                            modifier = Modifier
                                .padding(16.dp)
                                .noRippleClickable { onNavigateToRegister() },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }


            LaunchedEffect(uiState) {
                when (uiState) {
                    is LoginViewModel.UiState.Success -> {
                        onLoginSuccess()

                    }
                    is LoginViewModel.UiState.Error -> {
                        snackbarHostState.showSnackbar(
                            message = (uiState as LoginViewModel.UiState.Error).message
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = this.clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() }
) {
    onClick()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onLoginSuccess = {},
            viewModel = viewModel(
                factory = LoginViewModelFactory(LocalContext.current)
            )
        )
    }
}