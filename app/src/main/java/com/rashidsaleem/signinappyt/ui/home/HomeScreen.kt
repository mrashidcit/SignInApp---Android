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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateNext: (String) -> Unit,
    navigateBack: () -> Unit
) {
    HomeScreenContent(
        onEvent = { event ->
            when (event) {
                is HomeEvent.NavigateNext -> navigateNext(event.value)
                is HomeEvent.NavigateBack -> navigateBack()
                is HomeEvent.Logout -> {
                    viewModel.logout() {
                        navigateBack()
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "Name: "
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "email: "
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
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

