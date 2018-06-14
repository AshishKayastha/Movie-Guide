package com.ashish.movieguide.data.remote.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Distribution(
        @Json(name = "1") val one: Int? = null,
        @Json(name = "2") val two: Int? = null,
        @Json(name = "3") val three: Int? = null,
        @Json(name = "4") val four: Int? = null,
        @Json(name = "5") val five: Int? = null,
        @Json(name = "6") val six: Int? = null,
        @Json(name = "7") val seven: Int? = null,
        @Json(name = "8") val eight: Int? = null,
        @Json(name = "9") val nine: Int? = null,
        @Json(name = "10") val ten: Int? = null
)