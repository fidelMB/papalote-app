// feature/auth/AuthViewModel.kt
package com.example.papalote_app.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.papalote_app.model.UserData
import com.example.papalote_app.utils.Constants
import com.example.papalote_app.utils.ValidationResult
import com.example.papalote_app.utils.Validators
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val authState: StateFlow<AuthUiState> = _authState

    private val _userData = MutableStateFlow<UserData?>(UserData())
    val userData: StateFlow<UserData?> = _userData

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        auth.currentUser?.let {
            if (_authState.value !is AuthUiState.NotAuthenticated) {
                _authState.value = AuthUiState.Success
            }
        } ?: run {
            _authState.value = AuthUiState.NotAuthenticated
        }
    }

    fun updateEmail(email: String) {
        val validationResult = Validators.validateEmail(email)
        updateField(
            value = email,
            validation = validationResult,
            updateState = { state, fieldState -> state.copy(email = fieldState) }
        )
    }

    fun updatePassword(password: String) {
        val validationResult = Validators.validatePassword(password)
        updateField(
            value = password,
            validation = validationResult,
            updateState = { state, fieldState -> state.copy(password = fieldState) }
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val validationResult = Validators.validateConfirmPassword(
            _formState.value.password.value,
            confirmPassword
        )
        updateField(
            value = confirmPassword,
            validation = validationResult,
            updateState = { state, fieldState -> state.copy(confirmPassword = fieldState) }
        )
    }

    fun updateFullName(name: String) {
        val validationResult = Validators.validateName(name)
        updateField(
            value = name,
            validation = validationResult,
            updateState = { state, fieldState -> state.copy(fullName = fieldState) }
        )
    }

    fun updateBirthDate(date: String) {
        val validationResult = Validators.validateDate(date)
        updateField(
            value = date,
            validation = validationResult,
            updateState = { state, fieldState -> state.copy(birthDate = fieldState) }
        )
    }

    fun updateGender(gender: String) {
        _formState.update { it.copy(gender = gender) }
    }

    private fun updateField(
        value: String,
        validation: ValidationResult,
        updateState: (AuthFormState, AuthFieldState) -> AuthFormState
    ) {
        val fieldState = AuthFieldState(
            value = value,
            error = (validation as? ValidationResult.Invalid)?.message,
            isValid = validation is ValidationResult.Valid
        )
        _formState.update { updateState(it, fieldState) }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthUiState.Success

                getUserData(email)
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(mapFirebaseError(e))
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading

                // 1. Crear usuario en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email, password).await()

                // 2. Guardar información adicional en Firestore
                result.user?.uid?.let { uid ->
                    firestore.collection(Constants.USERS_COLLECTION)
                        .document(uid)
                        .set(
                            hashMapOf(
                                "fullName" to _formState.value.fullName.value,
                                "birthDate" to _formState.value.birthDate.value,
                                "gender" to _formState.value.gender,
                                "email" to email,
                                "createdAt" to System.currentTimeMillis()
                            )
                        ).await()
                }

                _authState.value = AuthUiState.Success
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(mapFirebaseError(e))
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthUiState.ResetEmailSent
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(mapFirebaseError(e))
            }
        }
    }

    fun signOut() {
        _authState.value = AuthUiState.NotAuthenticated
        auth.signOut()
        _formState.value = AuthFormState()
    }

    private fun mapFirebaseError(exception: Exception): AuthError {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
            is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
            is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
            is FirebaseAuthEmailException -> AuthError.EmailAlreadyInUse
            is FirebaseNetworkException -> AuthError.NetworkError
            else -> when {
                exception.message?.contains("too-many-requests") == true ->
                    AuthError.TooManyRequests
                else -> AuthError.Unknown(exception.message ?: "Error desconocido")
            }
        }
    }

    private suspend fun getUserData(userEmail: String) {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            // Query for the document where the "email" field matches userEmail
            if (userId != null) {
                val querySnapshot = firestore.collection("users")
                    .document(userId)  // Access the current user's document directly
                    .get()
                    .await()
                _userData.value = querySnapshot.toObject(UserData::class.java)
                println("USUARIO ${userData.value?.fullName} ${userData.value?.birthDate}")

            } else {
                // Handle the case where no document with the specified email was found
                println("No user found with email $userEmail")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}