// feature/auth/AuthState.kt
package com.example.papalote_app.features.auth

data class AuthFieldState(
    val value: String = "",
    val error: String? = null,
    val isValid: Boolean = true
)

data class AuthFormState(
    val email: AuthFieldState = AuthFieldState(),
    val password: AuthFieldState = AuthFieldState(),
    val confirmPassword: AuthFieldState = AuthFieldState(),
    val fullName: AuthFieldState = AuthFieldState(),
    val birthDate: AuthFieldState = AuthFieldState(),
    val gender: String = ""
) {
    val isValid: Boolean
        get() = email.isValid && password.isValid &&
                confirmPassword.isValid && fullName.isValid &&
                birthDate.isValid && gender.isNotBlank()
}

sealed class AuthError {
    object NetworkError : AuthError()
    object InvalidCredentials : AuthError()
    object UserNotFound : AuthError()
    object EmailAlreadyInUse : AuthError()
    object WeakPassword : AuthError()
    object TooManyRequests : AuthError()
    data class Unknown(val message: String) : AuthError()
}

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    object NotAuthenticated : AuthUiState()
    object ResetEmailSent : AuthUiState()
    data class Error(val error: AuthError) : AuthUiState()
}