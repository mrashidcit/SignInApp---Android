package com.rashidsaleem.signinappyt.ui.signin

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rashidsaleem.signinappyt.BuildConfig
import com.rashidsaleem.signinappyt.common.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val TAG = "SignInViewModel"
    var googleSignInClient: GoogleSignInClient? = null
    lateinit var auth: FirebaseAuth
    var mAccount: GoogleSignInAccount? = null

    private val _event: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val event: SharedFlow<UiEvent> = _event.asSharedFlow()

    fun initData(ctx: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(ctx as Activity, gso)

        auth = Firebase.auth
    }

    fun firebaseAuthWithGoogle(ctx: Context) {
        mAccount?.idToken?.let { idToken ->
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(ctx as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth?.currentUser
                        viewModelScope.launch {
                            _event.emit(
                                UiEvent.NavigateNext(Routes.home)
                            )
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        android.widget.Toast.makeText(
                            ctx,
                            "Unable to signin with Google",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    fun handleLauncherActivityResult(result: ActivityResult, ctx: Context) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            // Google Sign In was successful, authenticate with Firebase
            mAccount = task.getResult(ApiException::class.java)
            val idToken = mAccount?.idToken
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
        } catch (e: ApiException) {
            Log.e(TAG, "SignInScreen: ${e.message}", e)
        }
    }

    sealed class UiEvent {
        data class NavigateNext(val route: String): UiEvent()
    }

}