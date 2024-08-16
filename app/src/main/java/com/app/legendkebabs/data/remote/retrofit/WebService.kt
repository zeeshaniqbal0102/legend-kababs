package com.app.legendkebabs.data.remote.retrofit

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface WebService {

    @GET
    suspend fun get(
        @Url endpoint: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>
    ): retrofit2.Response<JsonElement>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>,
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<JsonElement>

    @GET
    suspend fun get(
        @Url endpoint: String
    ): retrofit2.Response<JsonElement>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @Header("token") header: String
    ): retrofit2.Response<JsonElement>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @Header("token") header: String,
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<JsonElement>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @Header("token") header: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, Any>,
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<JsonElement>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>,
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<JsonElement>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>
    ): retrofit2.Response<JsonElement>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Header("token") header: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>,
        @QueryMap params: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<JsonElement>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Header("token") header: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>
    ): retrofit2.Response<JsonElement>

    @POST
    suspend fun post(
        @Url endpoint: String
    ): retrofit2.Response<JsonElement>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Header("token") header: String
    ): retrofit2.Response<JsonElement>
}