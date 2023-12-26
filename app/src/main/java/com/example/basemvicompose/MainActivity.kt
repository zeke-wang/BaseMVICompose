package com.example.basemvicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.basemvicompose.config.RouterMap
import com.example.basemvicompose.ui.screen.admin.HomeView
import com.example.basemvicompose.ui.screen.home.AdminView
import com.example.basemvicompose.ui.screen.login.LoginView
import com.example.basemvicompose.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        //只要当页面为首页或者为设置页面,才展示底部菜单栏
//                        if (currentDestination?.hierarchy?.any { it.route == RouterMap.HOME_VIEW || it.route == RouterMap.ADMIN_VIEW } == true) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ) {
                            items.forEach { screen ->
                                currentDestination?.hierarchy?.any { it.route == screen.route }?.let {
                                    NavigationBarItem(
                                        label = { Text(stringResource(screen.resourceId)) },
                                        selected = it,
                                        icon = {
                                            Icon(
                                                screen.icon,
                                                contentDescription = stringResource(id = screen.resourceId)
                                            )
                                        },
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        })
                                }
                            }
                        }
//                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = "loginGraph",
                        Modifier.padding(innerPadding)
                    ) {
                        navigation(startDestination = RouterMap.HOME_VIEW, "mainGraph") {
                            composable(RouterMap.HOME_VIEW) { HomeView(navController) }
                            composable(RouterMap.ADMIN_VIEW) { AdminView(navController) }
                        }
                        navigation(startDestination = RouterMap.LOGIN_VIEW, "loginGraph") {
                            composable(RouterMap.LOGIN_VIEW) {
                                LoginView(onNavigateToMain = {
                                    navController.navigate("mainGraph") {
                                        popUpTo("loginGraph") {
                                            inclusive = true
                                        }
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}


val items = listOf(
    Screen.HomeScreen,
    Screen.AdminScreen,
)

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    data object HomeScreen : Screen(RouterMap.HOME_VIEW, R.string.home, Icons.Outlined.Home)
    data object AdminScreen :
        Screen(RouterMap.ADMIN_VIEW, R.string.admin, Icons.Outlined.AccountCircle)
}