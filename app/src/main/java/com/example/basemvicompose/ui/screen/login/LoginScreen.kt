package com.example.basemvicompose.ui.screen.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.basemvicompose.config.NavRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navigateTo: (route: String) -> Unit) {
    val vm = koinViewModel<LoginViewModel>()
    val loginRequestState by vm.loginRequestState.collectAsState()
    val tipShowState by vm.tipShowState.collectAsState()
    val snackBarState = remember { SnackbarHostState() }
    LaunchedEffect(loginRequestState) {
//        Log.d("LoginScreen","loginRequestState")
        if (loginRequestState == LoginRequestState.SUCCESS) {
            navigateTo(NavRoute.MainRouter.route)
        }
    }
    LaunchedEffect(tipShowState.showTip) {
        if (tipShowState.showTip) {
            snackBarState.showSnackbar(tipShowState.tipMsg)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarState) {
                Snackbar(
                    modifier = Modifier.padding(15.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(it.visuals.message, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var account by rememberSaveable { mutableStateOf("") }
            var isAccountError by rememberSaveable { mutableStateOf(false) }

            fun validateAccount(text: String) {
                isAccountError = text.isEmpty()
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = account,
                onValueChange = {
                    account = it
                    validateAccount(it)
                    vm.sendLoginInfoUpdateIntent(LoginInputChangeIntent.Username(it))
                },
                isError = isAccountError,
                label = { Text("账号") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "Localized description"
                    )
                },
            )
            var password by rememberSaveable { mutableStateOf("") }
            var passwordHidden by rememberSaveable { mutableStateOf(true) }
            var isPasswordError by rememberSaveable { mutableStateOf(false) }
            fun validatePassword(text: String) {
                isPasswordError = text.isEmpty() || text.length < 3
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                value = password,
                onValueChange = {
                    password = it
                    validatePassword(it)
                    vm.sendLoginInfoUpdateIntent(LoginInputChangeIntent.Password(it))
                },
                isError = isPasswordError,
                label = { Text("密码") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Localized description"
                    )
                },
                visualTransformation =
                if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val visibilityIcon =
                            if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            )
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    Log.d("Login", "$vm")
                    vm.sendLoginIntent(LoginIntent.LoginRequest)
                }) {
                Text(text = "登录")
            }
        }
    }
}