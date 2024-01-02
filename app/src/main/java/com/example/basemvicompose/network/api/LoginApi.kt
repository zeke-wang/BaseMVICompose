package com.example.basemvicompose.network.api

import com.example.basemvicompose.model.body.LoginBody
import com.example.basemvicompose.network.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginBody
    ): TokenResponse
}