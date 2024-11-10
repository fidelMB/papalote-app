// utils/ResultWrapper.kt
package com.example.papalote_app.utils

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}