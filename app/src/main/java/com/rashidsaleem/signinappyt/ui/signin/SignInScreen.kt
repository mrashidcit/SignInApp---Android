package com.rashidsaleem.signinappyt.ui.signin

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rashidsaleem.signinappyt.BuildConfig
import com.rashidsaleem.signinappyt.R
import com.rashidsaleem.signinappyt.common.Routes
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SignInScreen"

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateNext: (String) -> Unit,
) {
    val mContext = LocalContext.current



    LaunchedEffect(key1 = true) { // 1
        viewModel.initData(mContext)

        viewModel.event.collectLatest { event ->
            when(event) {
                is SignInViewModel.UiEvent.NavigateNext -> navigateNext(event.route)
            }
        }
    }



    val launcherActivityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleLauncherActivityResult(result, mContext)
    }

    val performSignIn by rememberUpdatedState {
        val signInIntent = viewModel.googleSignInClient?.signInIntent
//        Log.d(TAG, "SignInScreen: googleSignInClient , signInIntent = $googleSignInClient , $signInIntent")
        signInIntent?.let {
            launcherActivityResult.launch(signInIntent)    
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
                    performSignIn()
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

