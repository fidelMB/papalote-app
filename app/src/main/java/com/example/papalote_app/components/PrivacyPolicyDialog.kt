// PrivacyPolicyDialog.kt
package com.example.papalote_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Aviso de Privacidad") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = """
                    Al utilizar esta aplicación, usted acepta que recopilemos y usemos sus datos personales de acuerdo con esta política de privacidad. Sus datos serán utilizados para mejorar su experiencia y no serán compartidos con terceros sin su consentimiento.

                    Usted tiene derecho a acceder, rectificar y eliminar sus datos personales en cualquier momento.

                    Para más información, póngase en contacto con nuestro equipo de soporte.
                """.trimIndent())
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC4D600) // Establecemos el color #c4d600
                )
            ) {
                Text("Cerrar")
            }
        }
    )
}