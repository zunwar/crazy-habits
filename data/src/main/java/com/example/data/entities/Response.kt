package com.example.data.remote.network

import com.squareup.moshi.Json


data class ResponseError(
    @Json(name = "code") val code: Int?,
    @Json(name = "message") val message: String?,
)

data class HabitUID(
    @Json(name = "uid") val uid: String,
)