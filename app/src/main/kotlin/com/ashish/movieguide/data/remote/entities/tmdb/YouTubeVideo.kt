package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeVideo(
        val name: String?,
        val videoUrl: String,
        val imageUrl: String
)