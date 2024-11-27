// AuthNavigation.kt
package com.example.papalote_app.features.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.papalote_app.components.ResetPasswordDialog
import kotlinx.coroutines.launch

@Composable
fun AuthNavigation(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val isLogin by viewModel.isLogin.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Success -> onAuthSuccess()
            is AuthUiState.ResetEmailSent -> {
                showResetDialog = false
            }
            is AuthUiState.Error -> {
                val error = (authState as AuthUiState.Error).error
                errorMessage.value = when (error) {
                    AuthError.EmailAlreadyInUse -> "El correo ya está en uso, por favor intente con otro."
                    AuthError.WeakPassword -> "La contraseña es demasiado débil."
                    AuthError.InvalidCredentials -> "Credenciales inválidas."
                    AuthError.UserNotFound -> "Credenciales inválidas."
                    AuthError.NetworkError -> "Error de red. Por favor, revise su conexión."
                    AuthError.TooManyRequests -> "Demasiados intentos. Por favor, intente más tarde."
                    is AuthError.Unknown -> "Ha ocurrido un error: ${error.message}"
                }

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorMessage.value ?: "Error desconocido")
                }
            }
            else -> Unit
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        if (showResetDialog) {
            ResetPasswordDialog(
                onDismiss = { showResetDialog = false },
                onResetPassword = viewModel::resetPassword
            )
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (isLogin) {
                    SignIn(
                        formState = formState,
                        onEmailChange = viewModel::updateEmail,
                        onPasswordChange = viewModel::updatePassword,
                        onLogin = {
                            viewModel.signIn(formState.email.value, formState.password.value)
                        },
                        onForgotPassword = { showResetDialog = true },
                        onNavigateToRegister = { viewModel.setIsLogin(false) },
                        isLoading = authState is AuthUiState.Loading,
                        errorMessage = errorMessage.value
                    )
                } else {
                    Register(
                        formState = formState,
                        onFullNameChange = viewModel::updateFullName,
                        onBirthDateChange = viewModel::updateBirthDate,
                        onGenderChange = viewModel::updateGender,
                        onEmailChange = viewModel::updateEmail,
                        onPasswordChange = viewModel::updatePassword,
                        onConfirmPasswordChange = viewModel::updateConfirmPassword,
                        onAcceptedTermsChange = viewModel::updateAcceptedTerms,
                        onRegister = {
                            viewModel.signUp(formState.email.value, formState.password.value)
                        },
                        onNavigateToLogin = { viewModel.setIsLogin(true) },
                        isLoading = authState is AuthUiState.Loading,
                        errorMessage = errorMessage.value
                    )
                }
            }
        }
    }
}
