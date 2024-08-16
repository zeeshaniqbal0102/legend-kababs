package com.app.legendkebabs.data.remote

import com.google.gson.JsonElement
import com.app.legendkebabs.data.model.APIError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetApiError @Inject constructor(
    private val retrofit: Retrofit
) {
    fun parseError(response: Response<JsonElement>): APIError? {
        val converter: Converter<ResponseBody?, APIError> = retrofit
            .responseBodyConverter(APIError::class.java, arrayOfNulls<Annotation>(0))
        val error: APIError
        error = try {
            converter.convert(response.errorBody()!!)!!
        } catch (e: IOException) {
            return APIError()
        }
        error.statusCode = response.code()
        return error
    }
}