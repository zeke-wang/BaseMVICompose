package com.example.basemvicompose.ui.screen.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.basemvicompose.R
import com.example.basemvicompose.config.NavRoute
import com.example.basemvicompose.ui.screen.main.admin.AdminScreen
import com.example.basemvicompose.ui.screen.main.home.HomeScreen

@Composable
fun MainScreen() {
//    val showBottomBar = remember { mutableStateOf(true) }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            HomeBottomBar(
                destinations = topLevelScreens,
                currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                onNavigateToDestination = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = NavRoute.HomeRouter.route,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String,
) {

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(route = NavRoute.HomeRouter.route) {
            HomeScreen()
        }
        composable(route = NavRoute.AdminRouter.route) {
            AdminScreen()
        }
    }
}

@Composable
private fun HomeBottomBar(
    destinations: List<TopLevelScreen>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (route: String) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination.route) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.resourceId)
                    )
                })
        }
    }
}

val topLevelScreens = listOf(
    TopLevelScreen.HomeScreen,
    TopLevelScreen.AdminScreen,
)

sealed class TopLevelScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object HomeScreen : TopLevelScreen(
        NavRoute.HomeRouter.route,
        R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home
    )

    data object AdminScreen : TopLevelScreen(
        NavRoute.AdminRouter.route,
        R.string.admin,
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle
    )
}

