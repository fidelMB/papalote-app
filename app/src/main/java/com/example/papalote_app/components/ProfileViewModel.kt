package com.example.papalote_app.components

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.papalote_app.model.UserProfile
import com.example.papalote_app.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow(
        UserProfile(name = "John Doe", email = "johndoe@example.com", phone = "123-456-7890", url = "img")
    )
    val user: StateFlow<UserProfile> = _user

    private val firestore = FirebaseFirestore.getInstance()

    fun updateDefaultImage(selectedImageIndex: Int) {
        viewModelScope.launch {
            _user.value = _user.value.copy(url = selectedImageIndex.toString())

            // Guarda el índice de la imagen seleccionada en Firestore
            firestore.collection("users")
                .document(_user.value.email) // Asumiendo que el correo es único
                .update("profilePicture", selectedImageIndex)
                .addOnSuccessListener {
                    Log.d("ProfileViewModel", "Profile picture index updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileViewModel", "Error updating profile picture index", e)
                }
        }
    }
}