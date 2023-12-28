package com.example.basemvicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basemvicompose.config.NavRoute
import com.example.basemvicompose.ui.screen.login.LoginView
import com.example.basemvicompose.ui.screen.main.MainScreen
import com.example.basemvicompose.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoute.MainRouter.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = NavRoute.MainRouter.route) {
                        MainScreen()
                    }
                    composable(route = NavRoute.LoginRouter.route) {
                        LoginView {
                            navController.navigate(it)
                        }
                    }
                }
            }
        }
    }
}




