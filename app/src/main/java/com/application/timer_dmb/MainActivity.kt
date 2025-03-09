package com.application.timer_dmb

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.application.timer_dmb.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.MENU_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.PROFILE_SCREEN_ROUTE
import com.application.timer_dmb.presentation.calendar.view.calendar
import com.application.timer_dmb.presentation.choose_type.view.type
import com.application.timer_dmb.presentation.home.view.home
import com.application.timer_dmb.presentation.login.view.login
import com.application.timer_dmb.presentation.menu.view.menu
import com.application.timer_dmb.presentation.messanger.all_chats.view.allChats
import com.application.timer_dmb.presentation.messanger.chat.view.chat
import com.application.timer_dmb.presentation.profile.view.profile
import com.application.timer_dmb.presentation.registration.add_soldier.view.addSoldier
import com.application.timer_dmb.presentation.registration.otp.view.otp
import com.application.timer_dmb.presentation.registration.registration.view.registration
import com.application.timer_dmb.presentation.reset_password.confirm_password.view.confirmPassword
import com.application.timer_dmb.presentation.reset_password.otp_reset.view.otpReset
import com.application.timer_dmb.presentation.reset_password.reset.view.reset
import com.application.timer_dmb.presentation.set_photo.view.setPhoto
import com.application.timer_dmb.presentation.settings.view.settings
import com.application.timer_dmb.presentation.share_picture.view.sharePicture
import com.application.timer_dmb.presentation.splash.SplashScreenState
import com.application.timer_dmb.presentation.splash.SplashScreenViewModel
import com.application.timer_dmb.presentation.timer_settings.view.timerSettings
import com.application.timer_dmb.presentation.widget.DmbWidget
import com.application.timer_dmb.presentation.widget.TimeCounterWorker
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.Transparent
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.util.concurrent.TimeUnit

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val route: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {

        requestNotificationPermission()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)

        actionBar?.hide()



        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value == SplashScreenState.Idle
            }
        }

        setContent {
            LaunchedEffect(true) {
                Log.i("FCMToken", Firebase.messaging.token.await())
            }
            val navController = rememberNavController()
            val route by viewModel.route.collectAsState()
            val items = remember {
                mutableStateOf(
                    listOf(
                        BottomNavigationItem(title = "Главная", selectedIcon = R.drawable.home, route = HOME_SCREEN_ROUTE),
                        BottomNavigationItem(title = "Сообщения", selectedIcon = R.drawable.messages, route = "messages"),
                        BottomNavigationItem(title = "Меню", selectedIcon = R.drawable.menu, route = "menu")
                    )
                )
            }

            var selectedItemIndex = rememberSaveable {
                mutableIntStateOf(0)
            }

            val routesWithNav by remember {
                mutableStateOf(
                    listOf(
                        HOME_SCREEN_ROUTE,
                        MESSAGES_SCREEN_ROUTE,
                        MENU_SCREEN_ROUTE,
                        PROFILE_SCREEN_ROUTE,
                        CALENDAR_SCREEN_ROUTE
                    )
                )
            }

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            Spacer(modifier = Modifier.consumeWindowInsets(WindowInsets.navigationBars))
            MilitaryChatProjectTheme {

                Scaffold (
                    bottomBar = {
                        if(navBackStackEntry?.destination?.route in routesWithNav){
                            NavigationBar (
                                modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                                windowInsets = WindowInsets.navigationBars
                            ){
                                items.value.forEachIndexed{index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex.intValue == index,
                                        onClick = {
                                            selectedItemIndex.intValue = index
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                tint = if (index == selectedItemIndex.intValue) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                                                painter = painterResource(item.selectedIcon),
                                                contentDescription = item.title
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = Transparent
                                        ),
                                        label = {
                                            Text(text = item.title, style = MaterialTheme.typography.labelSmall)
                                        }
                                    )
                                }

                            }
                        }


                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = route) {
                            //With NavigationBar
                            home(navController, selectedItemIndex, innerPadding)
                            menu(navController)
                            allChats(navController)
                            calendar(navController)

                            //Without NavigationBar
                            registration(navController)
                            addSoldier(navController)
                            login(navController)
                            otp(navController)
                            profile(navController)
                            chat(navController)
                            settings(navController)
                            timerSettings(navController)
                            sharePicture(navController)
                            setPhoto(navController)
                            type(navController)
                            reset(navController)
                            otpReset(navController)
                            confirmPassword(navController)
                        }
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}