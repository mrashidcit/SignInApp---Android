package com.rashidsaleem.signinappyt.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.rashidsaleem.signinappyt.common.Routes
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme
import kotlinx.coroutines.delay

private const val SplashTimeInMiliseconds = 2000L

@Composable
fun SplashScreen(
    navigateNext: (String) -> Unit,
) {

    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState {
        navigateNext(Routes.signIn)
    }

    LaunchedEffect(true) {
        delay(SplashTimeInMiliseconds)
        currentOnTimeout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "SignIn App",
            fontSize = 30.sp
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SignInAppTheme {
        SplashScreen() {

        }
    }
}

