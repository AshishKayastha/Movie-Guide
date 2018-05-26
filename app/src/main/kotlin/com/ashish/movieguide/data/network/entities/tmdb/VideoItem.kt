package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoItem(
        val id: String? = null,
        val name: String? = null,
        val site: String? = null,
        val size: Int? = null,
        val key: String? = null,
        val type: String? = null
)