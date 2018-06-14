package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 19.
 */
@JsonClass(generateAdapter = true)
data class ProfileImages(val profiles: List<ImageItem>? = null)