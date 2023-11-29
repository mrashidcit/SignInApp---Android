package com.rashidsaleem.signinappyt.ui.home

import androidx.compose.runtime.remember
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

data class HomeUiState(
    val name: String = "",
    val email: String = "",
    val currentUser: FirebaseUser? = null
)
