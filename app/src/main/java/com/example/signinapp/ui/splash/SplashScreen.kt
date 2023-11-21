package com.example.signinapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        Text(
            text = "Splash Screen"
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {

}

