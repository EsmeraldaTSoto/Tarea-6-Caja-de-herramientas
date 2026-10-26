package com.example.cajaherramientas

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET suspend fun obj(@Url url: String): JsonObject
    @GET suspend fun arr(@Url url: String): JsonArray
}

object Net {
    val api: Api = Retrofit.Builder()
        .baseUrl("https://example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}