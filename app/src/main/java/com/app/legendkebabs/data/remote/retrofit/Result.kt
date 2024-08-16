package com.app.legendkebabs.data.remote.retrofit

import com.app.legendkebabs.data.model.APIError
import com.google.gson.JsonElement

sealed class Result {
    class Success(val response: JsonElement) : Result()
    object UnAuthorized : Result()
    class UnProcessable(val msg: String) : Result()
    class Error(val error: String) : Result()
    class Exception(val exception: java.lang.Exception) : Result()
    class RetrofitError(val apiError: APIError?) : Result()

}