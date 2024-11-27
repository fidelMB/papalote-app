// SignIn.kt
package com.example.papalote_app.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    isLoading: Boolean,
    errorMessage: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp, 16.dp, 15.dp, 32.dp)
            )

            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF1D1B20),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

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

            Spacer(modifier = Modifier.height(8.dp))

            AuthButton(
                text = "Iniciar sesión",
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                enabled = formState.email.isValid && formState.password.isValid
            )

            Spacer(modifier = Modifier.padding(bottom = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿No tienes cuenta? ", color = Color.Black)
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        "Regístrate",
                        color = Color(0xFF838f01),
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            TextButton(
                onClick = onForgotPassword
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = Color(0xFF838f01),
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
