package com.example.basemvicompose.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basemvicompose.model.body.LoginBody
import com.example.basemvicompose.model.state.TipShowState
import com.example.basemvicompose.network.RequestBuilder
import com.example.basemvicompose.network.RequestStatus
import com.example.basemvicompose.network.api.LoginApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val api: LoginApi
) : ViewModel() {
    //登录通过状态
    private var _loginRequestState = MutableStateFlow(LoginRequestState.NOTING)
    val loginRequestState = _loginRequestState.asStateFlow()

    //登录的信息
    private var _loginInfoState = MutableStateFlow(LoginInfoState())
    private val loginInfoState = _loginInfoState.asStateFlow()

    //提示显示状态
    private var _tipShowState = MutableStateFlow(TipShowState())
    val tipShowState = _tipShowState.asStateFlow()

    // handle =================
    fun sendLoginIntent(intent: LoginIntent) {
        Log.d("Login vm", "$intent")
        when (intent) {
            LoginIntent.LoginRequest -> loginRequest()
        }
    }

    fun sendLoginInfoUpdateIntent(intent: LoginInputChangeIntent) {
        when (intent) {
            is LoginInputChangeIntent.Username -> _loginInfoState.update {
                it.copy(username = intent.newUsername)
            }

            is LoginInputChangeIntent.Password -> _loginInfoState.update {
                it.copy(password = intent.newPassword)
            }
        }
    }

    private fun showTip(tipMsg: String) = viewModelScope.launch {
        _tipShowState.update {
            it.copy(showTip = true, tipMsg = tipMsg)
        }
        delay(1500)
        _tipShowState.update {
            it.copy(showTip = false, tipMsg = "")
        }
    }

    private fun loginRequest() = viewModelScope.launch {
        Log.d("Login vm", "loginReq")
        loginInfoState.value.apply {
            if (username == "") {
                showTip("账号不能为空")
                return@launch
            }
            if (password == "") {
                showTip("密码不能为空")
                return@launch
            }
        }
        _loginRequestState.update {
            LoginRequestState.LOADING
        }
        RequestBuilder.getToken {
            api.login(
                LoginBody(
                    username = loginInfoState.value.username,
                    password = loginInfoState.value.password
                )
            )
        }.collect {
            when (it) {
                is RequestStatus.Error -> {
//                    Log.e("TAG", "loginRequest:${it.errMsg} ${it.exception} ")
                    showTip(it.errMsg)
                }
                RequestStatus.Loading -> {}
                is RequestStatus.Success -> {
                    _loginRequestState.update {
                        LoginRequestState.SUCCESS
                    }
                    showTip("登录成功！")
                }
            }
        }
    }
}