package com.rashidsaleem.signinappyt.ui.signin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.rashidsaleem.signinappyt.BuildConfig
import com.rashidsaleem.signinappyt.common.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {
    private val TAG = "SignInViewModel"
    lateinit var auth: FirebaseAuth
    var account: GoogleSignInAccount? = null
        private set

    // Configure Google Sign In
    lateinit var gso: GoogleSignInOptions
        private set

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()


    init {
        auth = Firebase.auth

        gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_KEY)
            .requestEmail()
            .build()
    }

    fun firebaseAuthWithGoogle(ctx: Context) {

        account?.idToken?.let { idToken ->
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(ctx as android.app.Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        viewModelScope.launch {
                            _eventFlow.emit(
                                UiEvent.NavigateNext(Routes.home)
                            )
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(ctx, "SignIn Failed", android.widget.Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

    }

    fun processLauncherActivityResult(activityResult: ActivityResult, ctx: Context) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    Log.d(TAG, "Got ID token.")
                    firebaseAuthWithGoogle(ctx)
                }
                else -> {
                    // Shouldn't happen.
                    Log.d(TAG, "No ID token!")
                }
            }
        } catch (ex: ApiException) {
            Log.e(TAG, "SignInScreen: ${ex.message}", ex)
        }
    }


    sealed class UiEvent {
        data class NavigateNext(val route: String): UiEvent()
    }

}