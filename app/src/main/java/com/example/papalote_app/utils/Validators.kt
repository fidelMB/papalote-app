// utils/Validators.kt
package com.example.papalote_app.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Validators {
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("El email es requerido")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult.Invalid("El formato del email no es válido")
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("La contraseña es requerida")
            password.length < 6 -> ValidationResult.Invalid("La contraseña debe tener al menos 6 caracteres")
            !password.any { it.isDigit() } -> ValidationResult.Invalid("La contraseña debe contener al menos un número")
            !password.any { it.isLetter() } -> ValidationResult.Invalid("La contraseña debe contener al menos una letra")
            else -> ValidationResult.Valid
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Invalid("Confirma tu contraseña")
            password != confirmPassword -> ValidationResult.Invalid("Las contraseñas no coinciden")
            else -> ValidationResult.Valid
        }
    }

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("El nombre es requerido")
            name.length < 3 -> ValidationResult.Invalid("El nombre debe tener al menos 3 caracteres")
            else -> ValidationResult.Valid
        }
    }

    fun validateDate(date: String): ValidationResult {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)

            val inputDate = sdf.parse(date)
            val currentDate = Date()

            when {
                inputDate == null -> ValidationResult.Invalid("Fecha inválida")
                inputDate.after(currentDate) -> ValidationResult.Invalid("La fecha no puede ser futura")
                else -> ValidationResult.Valid
            }
        } catch (e: Exception) {
            ValidationResult.Invalid("Formato de fecha inválido")
        }
    }
}