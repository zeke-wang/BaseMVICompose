package com.example.basemvicompose.ui.screen.login

sealed class LoginIntent {
    data object LoginRequest : LoginIntent()
}

sealed class LoginInputChangeIntent {
    data class Password(val newPassword: String) : LoginInputChangeIntent()
    data class Username(val newUsername: String) : LoginInputChangeIntent()
}
