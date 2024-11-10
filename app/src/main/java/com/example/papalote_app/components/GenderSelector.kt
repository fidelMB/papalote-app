package com.example.papalote_app.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    value: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = when(value) {
                "M" -> "Hombre"
                "F" -> "Mujer"
                "X" -> "Prefiero no decirlo"
                else -> ""
            },
            onValueChange = { },
            readOnly = true,
            label = { Text("Género") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDBE78E),
                focusedLabelColor = Color(0xFFDBE78E)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Hombre") },
                onClick = {
                    onGenderSelected("M")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Mujer") },
                onClick = {
                    onGenderSelected("F")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Prefiero no decirlo") },
                onClick = {
                    onGenderSelected("X")
                    expanded = false
                }
            )
        }
    }
}