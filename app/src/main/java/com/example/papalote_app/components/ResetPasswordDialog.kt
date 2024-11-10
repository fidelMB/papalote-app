package com.example.papalote_app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResetPasswordDialog(
    onDismiss: () -> Unit,
    onResetPassword: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restablecer contraseña") },
        text = {
            Column {
                Text("Ingresa tu correo electrónico y te enviaremos instrucciones para restablecer tu contraseña.")
                Spacer(modifier = Modifier.height(16.dp))
                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo electrónico"
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onResetPassword(email) }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}