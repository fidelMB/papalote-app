package com.example.papalote_app.components

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.papalote_app.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow(UserProfile(name = "John Doe", email = "johndoe@example.com", phone = "123-456-7890", url = "img"))
    val user: StateFlow<UserProfile> = _user

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            _user.value = _user.value.copy(url = uri.toString())
        }
    }
}
