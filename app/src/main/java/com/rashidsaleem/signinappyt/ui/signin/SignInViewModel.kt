package com.rashidsaleem.signinappyt.ui.signin

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.rashidsaleem.signinappyt.BuildConfig
import com.rashidsaleem.signinappyt.common.AppConstants

class SignInViewModel: ViewModel() {

    private lateinit var signInRequest: BeginSignInRequest

    fun loginWithGoogle() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
    }

}