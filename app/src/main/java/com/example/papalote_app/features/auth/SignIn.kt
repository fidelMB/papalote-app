// feature/auth/SignIn.kt
package com.example.papalote_app.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.papalote_app.R
import com.example.papalote_app.components.AuthButton
import com.example.papalote_app.components.AuthTextField

@Composable
fun SignIn(
    formState: AuthFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp)
        )

        AuthTextField(
            value = formState.email.value,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            error = formState.email.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AuthTextField(
            value = formState.password.value,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            error = formState.password.error,
            isPassword = true,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                "¿Olvidaste tu contraseña?",
                color = Color(0xFFDBE78E)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AuthButton(
            text = "Iniciar Sesión",
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading,
            enabled = formState.email.isValid && formState.password.isValid
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("¿No tienes cuenta? ")
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    "Regístrate",
                    color = Color(0xFFDBE78E)
                )
            }
        }
    }
}