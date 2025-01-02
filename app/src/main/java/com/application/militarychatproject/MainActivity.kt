package com.application.militarychatproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.militarychatproject.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.CHAT_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.CONFIRM_PASSWORD_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.ENTER_CODE_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MENU_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.PROFILE_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.RESET_PASSWORD_SCREEN_ROUTE
import com.application.militarychatproject.presentation.calendar.view.calendar
import com.application.militarychatproject.presentation.home.view.home
import com.application.militarychatproject.presentation.login.view.login
import com.application.militarychatproject.presentation.menu.view.menu
import com.application.militarychatproject.presentation.messanger.all_chats.view.allChats
import com.application.militarychatproject.presentation.messanger.chat.view.chat
import com.application.militarychatproject.presentation.profile.view.profile
import com.application.militarychatproject.presentation.registration.registration.view.registration
import com.application.militarychatproject.presentation.registration.add_soldier.view.addSoldier
import com.application.militarychatproject.presentation.registration.otp.view.otp
import com.application.militarychatproject.presentation.splash.SplashScreenState
import com.application.militarychatproject.presentation.splash.SplashScreenViewModel
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.Transparent
import com.application.militarychatproject.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {

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
            val navController = rememberNavController()
            val route by viewModel.route.collectAsState()
            val state by viewModel.state.collectAsState()
            val items = listOf(
                BottomNavigationItem(title = "Главная", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = HOME_SCREEN_ROUTE),
                BottomNavigationItem(title = "Сообщения", selectedIcon = Icons.Filled.Email, unselectedIcon = Icons.Outlined.MailOutline, route = "messages"),
                BottomNavigationItem(title = "Меню", selectedIcon = Icons.Filled.Menu, unselectedIcon = Icons.Outlined.Menu, route = "menu")
            )
            var selectedItemIndex by rememberSaveable {
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

            val routesWithTopBar by remember{
                mutableStateOf(
                    listOf(
                        LOGIN_SCREEN_ROUTE,
                        RESET_PASSWORD_SCREEN_ROUTE,
                        CONFIRM_PASSWORD_SCREEN_ROUTE,
                        ENTER_CODE_SCREEN_ROUTE,
                        PROFILE_SCREEN_ROUTE
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
                                items.forEachIndexed{index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
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
                                                imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
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


                    },
                    topBar = {
                        AnimatedVisibility(
                            visible = navBackStackEntry?.destination?.route in routesWithTopBar,
                            enter = slideInVertically(initialOffsetY = { -it }),
                            exit = slideOutVertically(targetOffsetY = { -it })
                        ) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = White,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        text = "Назад",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                },
                            )
                        }

                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = route) {
                            //With NavigationBar
                            home(navController)
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
                        }
                    }
                }
            }
        }
    }
}