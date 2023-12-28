package com.example.basemvicompose.config

sealed class NavRoute(val route: String) {
    data object MainRouter : NavRoute("main")
    data object LoginRouter : NavRoute("login")
    data object HomeRouter:NavRoute("home")
    data object AdminRouter:NavRoute("admin")
}