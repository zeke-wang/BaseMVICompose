package com.example.basemvicompose.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoginView(onNavigateToMain: () -> Unit) {
    // 使用 Column 撑满父布局
    Column(
        modifier = Modifier
            .fillMaxSize() // 这里设置撑满父布局
            .background(Color.Gray)
    ) {
        Button(
            onClick = onNavigateToMain,
        ) {
            Text(text = "click")
        }
    }
}