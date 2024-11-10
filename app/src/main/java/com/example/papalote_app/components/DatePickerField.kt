package com.example.papalote_app.components

import android.app.DatePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DatePickerField(
    value: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val context = LocalContext.current

    AuthTextField(
        value = value,
        onValueChange = { },
        label = "Fecha de nacimiento",
        modifier = modifier,
        error = error,
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                            onDateSelected(date)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha"
                )
            }
        }
    )
}