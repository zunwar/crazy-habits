package com.example.data.remote.network


import kotlinx.coroutines.*
import okhttp3.Request
import okio.Timeout
import org.json.JSONObject
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

sealed class NetworkResult<T : Any> {

    class Success<T : Any>(val data: T, val code: Int, val message: String?) : NetworkResult<T>()

    class Error<T : Any>(val code: Int, val message: String?, val responseError: ResponseError? = null) : NetworkResult<T>()

    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()

}

suspend fun <T : Any> handleApi(execute: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            NetworkResult.Success(data = body, code = response.code(), message = response.message())
        } else {
            val errorBody = response.errorBody()!!.string()
            val errorBodyJson = JSONObject(errorBody)
            val responseError =
                ResponseError(
                    errorBodyJson.getInt("code"),
                    errorBodyJson.getString("message")
                )
            NetworkResult.Error(
                code = response.code(),
                message = response.message(),
                responseError = responseError
            )
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}


class NetworkResultCall<T : Any>(
    private val proxy: Call<T>,
) : Call<NetworkResult<T>> {


    override fun enqueue(callback: Callback<NetworkResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                CoroutineScope(Dispatchers.IO).launch {
                    val networkResult = handleApi { response }
                    callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = NetworkResult.Exception<T>(t)
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<NetworkResult<T>> = throw NotImplementedError()
    override fun clone(): Call<NetworkResult<T>> = NetworkResultCall(proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() {
        proxy.cancel()
    }
}

class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResult<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> {
        return NetworkResultCall(call)
    }
}

class NetworkResultCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != NetworkResult::class.java) {
            return null
        }

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResultCallAdapter(resultType)
    }

    companion object {
        fun create(): NetworkResultCallAdapterFactory = NetworkResultCallAdapterFactory()
    }
}