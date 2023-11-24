package com.rashidsaleem.signinappyt.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rashidsaleem.signinappyt.common.Routes
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateNext: (String, Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    HomeScreenContent(
        onEvent = { event ->
            when (event) {
                is HomeEvent.NavigateNext -> navigateNext(event.value, false)
                is HomeEvent.NavigateBack -> navigateBack()
                is HomeEvent.Logout -> {
                    viewModel.logout() {
                        navigateNext(Routes.signIn, true)
                    }
                }
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    onEvent: (HomeEvent) -> Unit
) {
    val currentUser = remember {
        Firebase.auth.currentUser
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "Name: ${currentUser?.displayName}"
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "email: ${currentUser?.email}"
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            Firebase.auth.signOut()
            onEvent(HomeEvent.Logout)
        }) {
            Text(text = "Logout")
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SignInAppTheme {
        HomeScreenContent(onEvent = {

        })
    }
}

