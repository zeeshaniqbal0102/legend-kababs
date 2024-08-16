package com.app.legendkebabs.data.remote.repo

import android.util.Log
import android.util.MalformedJsonException
import com.google.gson.JsonElement
import com.app.legendkebabs.ConnectivityException
import com.app.legendkebabs.data.remote.Connectivity
import com.app.legendkebabs.data.remote.GetApiError
import com.app.legendkebabs.data.remote.retrofit.WebService
import com.app.legendkebabs.data.remote.retrofit.Result
import javax.inject.Inject

class NetworkCall @Inject constructor(
    val webService: WebService,
    val connectivity: Connectivity,
    val getApiError: GetApiError
) {

    inline fun <reified T : Any> generalRequest(
        request: () -> retrofit2.Response<JsonElement>
    ): Result =
        try {
            val response = request()
            Log.d("GENERAL_REQ", "Headers---> ${response.headers()}")
            Log.d("GENERAL_REQ", "RawResponse ---> ${response.raw()}")
            if (response.isSuccessful) {
                Log.d("GENERAL_REQ", "RawResponse ---> ${response.body()}")
                if (response.body() != null) {
                    Result.Success(
                        response.body()!!
                    )
                } else {
                    println("Error")
                    Result.Error(
                        response.message()
                    )
                }
            } else {
                println("RetrofitError")
//              to handle response codes other than 200
                Result.RetrofitError(getApiError.parseError(response))
//                Result.RetrofitError(getApiError.parseError(response.errorBody()!!))
            }
        } catch (exception: Exception) {
//              this catch is like onFailure when we used callbacks
            Log.d("GENERAL_REQ", "Exception ${exception.message!!}")


            if (exception is MalformedJsonException){
                val response = request()

            Log.d("file", "Exception ${response.errorBody()?.byteStream()}")
            }
//               this is custom exception to show network error
            Result.Exception(exception)
        }


    suspend inline fun <reified T : Any> get(
        endpoint: String, //end point
        headerToken: String?, //end point
        queryMap: Map<String, String>?, //query params
        params: Map<String, Any>? = null //query params
    ): Result {
        Log.d("GENERAL_REQ", "endpoint : $endpoint")
        Log.d("GENERAL_REQ", "headerToken : $headerToken")
        Log.d("GENERAL_REQ", "queryMap: $queryMap")
        //        if no internet connection available throw exception and return
        if (!connectivity.isNetworkConnected()) {
            return Result.Exception(ConnectivityException())
        }
        if (queryMap == null) {
            if (headerToken == null)
                return generalRequest<T> { webService.get(endpoint) }
            else
                if (params == null)
                    return generalRequest<T> { webService.get(endpoint, headerToken) }
                else
                    return generalRequest<T> { webService.get(endpoint, headerToken, params) }
        } else {
            if (headerToken == null)
                if (params == null)
                    return generalRequest<T> { webService.get(endpoint, queryMap) }
                else
                    return generalRequest<T> { webService.get(endpoint, queryMap, params) }
            else
                if (params == null)
                    return generalRequest<T> { webService.get(endpoint, headerToken, queryMap) }
                else
                    return generalRequest<T> { webService.get(endpoint, headerToken, queryMap, params) }
        }
    }


    suspend inline fun <reified T : Any> post(
        endpoint: String, //end point
        headerToken: String?, //end p
        queryMap: Map<String, String>?, //query params
        params: Map<String, Any>? = null //query params
    ): Result {
        Log.d("GENERAL_REQ", "endpoint : $endpoint")
        Log.d("GENERAL_REQ", "queryMap: $queryMap")
//        if no internet connection available throw exception and return
        if (!connectivity.isNetworkConnected()) {
            return Result.Exception(ConnectivityException())
        }
        if (queryMap == null)
            if (headerToken == null)
                return generalRequest<T> { webService.post(endpoint) }
            else
                return generalRequest<T> { webService.post(endpoint, headerToken) }
        else
            if (headerToken == null)
                if (params == null)
                    return generalRequest<T> { webService.post(endpoint, queryMap) }
                else
                    return generalRequest<T> { webService.post(endpoint, queryMap, params) }
            else
                if (params == null)
                    return generalRequest<T> { webService.post(endpoint, headerToken, queryMap) }
                else
                    return generalRequest<T> { webService.post(endpoint, headerToken, queryMap, params) }
    }
}

