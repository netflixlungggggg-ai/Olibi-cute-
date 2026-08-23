package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.repository.BoosterRepository
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainContainerScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.OlibiBoosterTheme

enum class AppNavState {
    SPLASH,
    LOGIN,
    MAIN
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OlibiBoosterTheme {
                var navState by remember { mutableStateOf(AppNavState.SPLASH) }

                Crossfade(
                    targetState = navState,
                    modifier = Modifier.fillMaxSize(),
                    label = "app_navigation_crossfade"
                ) { state ->
                    when (state) {
                        AppNavState.SPLASH -> {
                            SplashScreen(
                                onSplashFinished = {
                                    navState = AppNavState.MAIN
                                }
                            )
                        }

                        AppNavState.LOGIN -> {
                            LoginScreen(
                                onLoginSuccess = { name, email ->
                                    BoosterRepository.updateProfile(name, email)
                                    navState = AppNavState.MAIN
                                }
                            )
                        }

                        AppNavState.MAIN -> {
                            MainContainerScreen(
                                onSignOut = {
                                    navState = AppNavState.LOGIN
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
