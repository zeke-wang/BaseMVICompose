package com.example.basemvicompose.network.response

data class BaseResponse<T>(val status: Int, val msg: String, val data: T?)
data class TokenResponse(val code: Int, val msg: String, val token: String)
object ResponseStatus {
    const val SUCCESS = 200
    const val ERROR = 500
}