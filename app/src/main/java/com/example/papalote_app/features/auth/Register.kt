// feature/auth/Register.kt
package com.example.papalote_app.features.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.papalote_app.R
import com.example.papalote_app.components.AuthButton
import com.example.papalote_app.components.AuthTextField
import com.example.papalote_app.components.DatePickerField
import com.example.papalote_app.components.GenderSelector

@Composable
fun Register(
    formState: AuthFormState,
    onFullNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
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

        Text(
            text = "Regístrate",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF1D1B20),
                fontWeight = FontWeight(600)
            ),
            modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
        )

        AuthTextField(
            value = formState.fullName.value,
            onValueChange = onFullNameChange,
            label = "Nombre completo",
            error = formState.fullName.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DatePickerField(
            value = formState.birthDate.value,
            onDateSelected = onBirthDateChange,
            error = formState.birthDate.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GenderSelector(
            value = formState.gender,
            onGenderSelected = onGenderChange,
            modifier = Modifier.padding(bottom = 8.dp)
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

        AuthTextField(
            value = formState.confirmPassword.value,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña",
            error = formState.confirmPassword.error,
            isPassword = true,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AuthButton(
            text = "Registrarse",
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading,
            enabled = formState.isValid
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Ya tienes cuenta? ")
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    "Inicia sesión",
                    color = Color(0xFFDBE78E)
                )
            }
        }
    }
}