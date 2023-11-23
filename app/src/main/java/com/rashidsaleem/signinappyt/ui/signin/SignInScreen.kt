package com.rashidsaleem.signinappyt.ui.signin

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.rashidsaleem.signinappyt.BuildConfig
import com.rashidsaleem.signinappyt.R
import com.rashidsaleem.signinappyt.common.AppConstants
import com.rashidsaleem.signinappyt.common.Routes
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme

private val REQ_ONE_TAP = 2
private const val TAG = "SignInScreen"

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateNext: (String) -> Unit,
) {
    val mContext = LocalContext.current

//    val signInRequest = remember {
//        BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
////                    .setServerClientId(getString(R.string.your_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            )
//            .build()
//    }

    var googleSignInClient = remember {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_KEY)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(mContext, gso)
    }



    var auth: FirebaseAuth = remember {
        Firebase.auth
    }

    var account: GoogleSignInAccount? = remember {
        null
    }

    var currentUser: FirebaseUser? = remember {
        null
    }

    val firebaseAuthWithGoogle by rememberUpdatedState {
        account?.let { account ->
            if (account.idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(mContext as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            navigateNext(Routes.home)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                        }
                    }
            }
        }


    }

    val launcherForActivityResult =  rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
//        contract = ActivityResultContracts.GetContent()
    ) { activityResult ->
//        activityResult.resultCode
        Log.d(TAG, "SignInScreen - activityResult: resultCode = ${activityResult.resultCode}")

        try {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            val googleCredential = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            account = googleCredential.result
            val idToken = account?.idToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    Log.d(TAG, "Got ID token.")
                    firebaseAuthWithGoogle()
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

    val performSignWithGoogle by rememberUpdatedState {
        Log.d(TAG, "SignInScreen: performSignWithGoogle")
        val signInIntent = googleSignInClient.signInIntent
        try {
//            startActivityForResult(
//                mContext as Activity,
//                signInIntent,
//                1000,
//                null
//            )
            launcherForActivityResult.launch(
                signInIntent,
                null
            )
        } catch (ex: Exception) {
            Log.e(TAG, "SignInScreen: ${ex.message}", ex)
        }

    }

    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White,
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Blue.copy(0.5f),
                    shape = RoundedCornerShape(12.dp),
                )
                .clickable {
                    performSignWithGoogle()
//                    navigateNext(Routes.home)
                }
                .padding(12.dp)
                .padding(horizontal = 24.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_google_icon),
                contentDescription = null,
                )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "SignIn with Google",
                textAlign = TextAlign.Center
            )
        }

    }
}


@Preview
@Composable
fun SignInScreenPreview() {
    SignInAppTheme {
        SignInScreen() {

        }
    }
}

