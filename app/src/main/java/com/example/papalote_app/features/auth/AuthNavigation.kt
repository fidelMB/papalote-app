// feature/auth/AuthNavigation.kt
package com.example.papalote_app.features.auth

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papalote_app.components.ResetPasswordDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

@Composable
fun AuthNavigation(
    onAuthSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    var isLogin by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Success -> onAuthSuccess()
            is AuthUiState.ResetEmailSent -> {
                showResetDialog = false
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

        if (isLogin) {
            SignIn(
                formState = formState,
                onEmailChange = viewModel::updateEmail,
                onPasswordChange = viewModel::updatePassword,
                onLogin = {
                    viewModel.signIn(formState.email.value, formState.password.value)
                },
                onForgotPassword = { showResetDialog = true },
                onNavigateToRegister = { isLogin = false },
                isLoading = authState is AuthUiState.Loading
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
                onRegister = {
                    viewModel.signUp(formState.email.value, formState.password.value)
                },
                onNavigateToLogin = { isLogin = true },
                isLoading = authState is AuthUiState.Loading
            )
        }
    }
}