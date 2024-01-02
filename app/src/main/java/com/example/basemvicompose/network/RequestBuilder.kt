package com.example.basemvicompose.network

import android.util.Log
import com.example.basemvicompose.constant.REQUEST_TIMEOUT
import com.example.basemvicompose.network.response.ResponseStatus
import com.example.basemvicompose.network.response.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object RequestBuilder {
    private const val TAG = "RequestBuilder"
    private var retrofitBuilder: Retrofit
//    private const val BASE_URL = "http://192.168.1.135:8090"
    private const val BASE_URL = "http://140.210.194.160:8090"

    // 日志拦截器
    private val logInterceptor = HttpLoggingInterceptor {
        Log.d(TAG, "OkHttp Log : $it")
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    init {
        OkHttpClient.Builder()
//            .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)))
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .build()
            .apply {
                retrofitBuilder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(this)
                    .build()
            }
    }

    /**
     * 获取APi接口的实体请求类
     *
     * @param T
     * @param apiType
     * @return
     */
    fun <T : Any> getAPI(apiType: KClass<out T>): T = retrofitBuilder.create(apiType.java)

    /**
     * TODO 处理网络请求
     * @param requestFun Function0<Response<T>>
     * @return Flow<RequestStatus<T>>
     */
    suspend fun <T> getResponse(requestFun: () -> Response<T>): Flow<RequestStatus<T>> =
        flow {
            emit(RequestStatus.Loading)
            try {
                with(requestFun()) {
                    if (isSuccessful) {
                        if (body() != null) {
                            RequestStatus.Success(body()!!)
                        } else RequestStatus.Error(
                            Exception("失败"),
                            "服务器异常"
                        )
                    } else {
                        RequestStatus.Error(Exception("${code()}"), "网络不通")
                    }.let {
                        emit(it)
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "getResponse:${e.message} ")
                emit(RequestStatus.Error(e, "网络请求发送失败"))
            }
        }.catch { e ->
            emit(RequestStatus.Error(e as Exception, "网络请求无法开始"))
        }.flowOn(Dispatchers.IO)

    suspend fun getToken(request: suspend () -> TokenResponse): Flow<RequestStatus<String>> =
        flow {
            emit(RequestStatus.Loading)
            val baseData = request.invoke()
            Log.d(TAG, "getToken -> $baseData")
            if (baseData.code == ResponseStatus.SUCCESS) {
                //正确
                emit(RequestStatus.Success(baseData.token))
            } else {
                // ToastUtil.showMsg()
                emit(RequestStatus.Error(Exception("${baseData.code}"), baseData.msg))
            }
        }.catch { e ->
            emit(RequestStatus.Error(e as Exception, "网络请求无法开始"))
        }
}

sealed class RequestStatus<out T> {
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Error(val exception: Exception, val errMsg: String) : RequestStatus<Nothing>()
    object Loading : RequestStatus<Nothing>()
}