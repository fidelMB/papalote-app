// AuthViewModel.kt
package com.example.papalote_app.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.papalote_app.model.Activity
import com.example.papalote_app.model.Event
import com.example.papalote_app.model.UserData
import com.example.papalote_app.utils.Constants
import com.example.papalote_app.utils.ValidationResult
import com.example.papalote_app.utils.Validators
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
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

    private val _isLogin = MutableStateFlow(true)
    val isLogin: StateFlow<Boolean> = _isLogin

    fun setIsLogin(value: Boolean) {
        _isLogin.value = value
        resetFormState()
    }

    private fun resetFormState() {
        _formState.value = AuthFormState()
    }

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthUiState.Success

            viewModelScope.launch {
                loadUserData(currentUser.uid)
            }
        } else {
            _authState.value = AuthUiState.NotAuthenticated
        }
    }

    private suspend fun loadUserData(userId: String) {
        try {
            val userDocument = firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            val userData = userDocument.toObject(UserData::class.java)
            if (userData != null) {
                userData.userId = userId
                _userData.value = userData
            } else {
                _authState.value = AuthUiState.NotAuthenticated
                auth.signOut()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _authState.value = AuthUiState.Error(AuthError.Unknown(e.message ?: "Error desconocido"))
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

    fun updateAcceptedTerms(accepted: Boolean) {
        _formState.update { it.copy(acceptedTerms = accepted) }
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
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthUiState.Success

                result.user?.uid?.let { uid ->
                    loadUserData(uid)
                }
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(mapFirebaseError(e))
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading

                val result = auth.createUserWithEmailAndPassword(email, password).await()

                val events = firestore.collection("events")
                    .get()
                    .await()
                    .documents.map { document -> document.toObject(Event::class.java)!! }

                result.user?.uid?.let { uid ->
                    firestore.collection(Constants.USERS_COLLECTION)
                        .document(uid)
                        .set(
                            hashMapOf(
                                "fullName" to _formState.value.fullName.value,
                                "birthDate" to _formState.value.birthDate.value,
                                "gender" to _formState.value.gender,
                                "email" to email,
                                "createdAt" to System.currentTimeMillis(),
                                "activities" to mutableListOf<Activity>(),
                                "events" to events
                            )
                        ).await()

                    loadUserData(uid)
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
            is FirebaseAuthInvalidUserException -> AuthError.InvalidCredentials
            is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
            is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
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
            if (userId != null) {
                val querySnapshot = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                _userData.value = querySnapshot.toObject(UserData::class.java)
                _userData.value?.userId = userId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
