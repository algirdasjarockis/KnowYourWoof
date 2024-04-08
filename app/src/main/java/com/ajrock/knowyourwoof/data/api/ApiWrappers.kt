package com.ajrock.knowyourwoof.data.api

import android.util.Log
import com.google.gson.Gson
import retrofit2.Response

data class WrappedResponse<T>(
    val data: T?,
    val isError: Boolean,
    val httpCode: Int,
    val errorMessage: String
)

data class RawErrorResponse(
    val message: String? = null,
    val status: String? = null,
    val code: Int? = null
)

inline fun <T> apiRequestWrapper(call: () -> Response<T>): WrappedResponse<T> {
    try {
        val res = call()

        return if (res.isSuccessful && res.body() != null) {
            WrappedResponse(res.body(), false, res.code(), "")
        } else {
            val errMsg = res.errorBody()?.string()?.let {
                val error = Gson().fromJson(it, RawErrorResponse::class.java)
                error.message
            } ?: res.code().toString()

            WrappedResponse(null, true, res.code(), errMsg)
        }
    } catch (e: Exception) {
        Log.e("apiRequestWrapper", "Error while calling given callback: '${e.message}'")
        return WrappedResponse(null, true, -1, e.message ?: "")
    }
}