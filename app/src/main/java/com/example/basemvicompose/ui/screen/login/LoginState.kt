package com.example.basemvicompose.ui.screen.login

data class LoginInfoState(
    var username:String="",
    var password:String=""

)
enum class LoginRequestState{
    LOADING,
    ERR,
    SUCCESS,
    NOTING
}