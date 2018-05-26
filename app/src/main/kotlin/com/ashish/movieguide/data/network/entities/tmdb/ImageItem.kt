package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageItem(
        val id: Long? = null,
        @Json(name = "file_path") val filePath: String? = null
)