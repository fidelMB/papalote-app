// Register.kt
package com.example.papalote_app.features.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.papalote_app.components.*

@Composable
fun Register(
    formState: AuthFormState,
    onFullNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptedTermsChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    isLoading: Boolean,
    errorMessage: String? = null
) {
    var showPrivacyPolicy by remember { mutableStateOf(false) }

    if (showPrivacyPolicy) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyPolicy = false })
    }

    BackHandler(onBack = onNavigateToLogin)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Regístrate",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF1D1B20),
                fontWeight = FontWeight(600)
            ),
            modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
        )

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = formState.acceptedTerms,
                onCheckedChange = onAcceptedTermsChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFC4D600),
                    uncheckedColor = Color(0xFFC4D600),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Acepto los ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "términos y condiciones",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF838f01)),
                modifier = Modifier.clickable { showPrivacyPolicy = true }
            )
        }

        AuthButton(
            text = "Registrarse",
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading,
            enabled = formState.isValid
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Ya tienes cuenta? ")
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    "Inicia sesión",
                    color = Color(0xFF838f01),
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
