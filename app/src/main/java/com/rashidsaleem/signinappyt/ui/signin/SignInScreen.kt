package com.rashidsaleem.signinappyt.ui.signin

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.foundation.pager.rememberPagerState
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rashidsaleem.signinappyt.R
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SignInScreen"

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateNext: (String) -> Unit,
) {
    val mContext = LocalContext.current

    val googleSignInClient = remember {
        GoogleSignIn.getClient(mContext as Activity, viewModel.gso)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow
            .collectLatest { event ->
                when(event) {
                    is SignInViewModel.UiEvent.NavigateNext -> navigateNext(event.route)
                }
            }
    }

    val launcherActivityResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        viewModel.processLauncherActivityResult(activityResult, mContext)
    }
    
    val performSignInRequest by rememberUpdatedState {
        val signInIntent = googleSignInClient.signInIntent
        launcherActivityResult.launch(signInIntent)
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
//                    navigateNext(Routes.home)
                    performSignInRequest()
                }
                .padding(12.dp)
                .padding(horizontal = 24.dp),
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

