package com.rashidsaleem.signinappyt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rashidsaleem.signinappyt.ui.theme.SignInAppTheme
import com.rashidsaleem.signinappyt.common.Routes
import com.rashidsaleem.signinappyt.ui.home.HomeScreen
import com.rashidsaleem.signinappyt.ui.signin.SignInScreen
import com.rashidsaleem.signinappyt.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            SignInAppTheme {

                NavHost(
                    navController = navController,
                    startDestination = Routes.splash
                ) {

                    composable(Routes.splash) {
                        SplashScreen() { route ->
                            navController.navigate(route)
                        }
                    }

                    composable(Routes.signIn) {
                        SignInScreen(navigateNext = { route ->
                            navController.navigate(route)
                        })
                    }

                    composable(Routes.home) {
                        HomeScreen(navigateNext = { route ->
                            navController.navigate(route)
                        },
                            navigateBack = {
                                navController.popBackStack(Routes.signIn, inclusive = false)
                            }
                        )
                    }

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SignInAppTheme {
        Greeting("Android")
    }
}