package com.application.militarychatproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.application.militarychatproject.presentation.profile.view.profile
import com.application.militarychatproject.presentation.registration.first.view.registrationFirst
import com.application.militarychatproject.presentation.splash.SplashScreenState
import com.application.militarychatproject.presentation.splash.SplashScreenViewModel
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        actionBar?.hide()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value == SplashScreenState.Idle
            }
        }

        setContent {
            val route by viewModel.route.collectAsState()
            MilitaryChatProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = route) {
                        profile(navController)
                        registrationFirst(navController)
                    }
                }
            }
        }
    }
}