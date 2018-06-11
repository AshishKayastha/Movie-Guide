package com.ashish.movieguide.data.network.entities.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ratings(
        @Json(name = "Source") val source: String? = null,
        @Json(name = "Value") val value: String? = null
) {

    fun getRatingValue(): String? = value?.substringBefore("/")
}