package com.example.basemvicompose.di

import com.example.basemvicompose.network.RequestBuilder
import com.example.basemvicompose.network.api.LoginApi
import com.example.basemvicompose.ui.screen.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single { RequestBuilder.getAPI(LoginApi::class) }
    viewModel { LoginViewModel(get()) }
}

val appModules = mutableListOf(
    loginModule
)